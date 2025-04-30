package server;

import mock.*; // TODO: temp
import shared.*;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;

public class Server {
	private final static int PORT = 7777; // server port number
	private final static String LOGS_DIR = "/logs/"; // saved logs directory
	private final static String LOG_PREFIX = "server_"; // log filename prefix
	private final static String DATA_DIR = "/data/"; //server data directory
	
	public static void main(String[] args) {
		Log log = new Log(Paths.get(System.getProperty("user.dir"), LOGS_DIR).toString(), LOG_PREFIX); // create new log for server instance
		// load server data from file
		ServerData serverData = new ServerData(DATA_DIR, log);
		serverData.loadAll();
		// starting connection
		try (ServerSocket ss = new ServerSocket(PORT)) {
			ss.setReuseAddress(true); // ensures server uses same ip address
			log.append("Server started at IP: "+ss.getInetAddress().getHostAddress());
			// server loop
			while (true) {
				Socket client = ss.accept();
				ClientHandler ch = new ClientHandler(client, log, serverData);
				new Thread(ch).start(); // client is handled in new thread
			}
		// exception handling
		} catch (IOException e) {
			log.append(LogType.ERROR, e+" in client reception"); // log error message to file
		}
	}
	
	private static class ClientHandler implements Runnable {
		private final Socket client;
		private final Log log;
		private boolean isEmployee;
		private Employee employee; // employee object needed to execute employee-only commands (provided upon login)
		private Garage garage;
		private ServerData serverData;
		ObjectOutputStream out; // outgoing messages to client
		ObjectInputStream in; // incoming messages from client
		
		public ClientHandler(Socket client, Log log, ServerData serverData) {
			this.client = client;
			this.log = log;
			this.serverData = serverData;
		}
		
		public void run() {
			log.append(client.getInetAddress().getHostAddress()+" has connected"); // TODO: maybe include client id/type too?
			try {
				out = new ObjectOutputStream(client.getOutputStream());
				in = new ObjectInputStream(client.getInputStream());
				listenInit(); // listen for init message
				if (isEmployee) { // if listenInit() found employee role
					handleEmployee(); // handle client as employee
				} else { 
					handleCustomer(); // handle client as customer instead
				}
			} catch (IOException e) {
				log.append(LogType.ERROR, e+" in client communication"); // TODO: identify which client caused this error (ip or some other id)
			}
		}
		
		// helper methods
		private void sendMessage(MessageType type, String text) {
			try {
				out.writeObject(new Message(type, "server", text));
			} catch (IOException e) {
				log.append(LogType.ERROR, e+" while attempting to send message to client ("+client+")");
			}
		}
		
		private void listenInit() {
			Message incoming;
			while (true) {
				try {
					incoming = (Message)in.readObject();
					if (isValidInit(incoming)) { // checks if init message is valid
						String split[] = incoming.getText().split(":");
						String garageID = split[1];
						String role = split[2].toLowerCase();
						// assign garage
						garage = serverData.getGarage(garageID);
						if (garage == null) { // if no garage was found
							sendMessage(MessageType.Fail, "init:nonexistent_garage");
							log.append(LogType.ERROR, client+" requested nonexistent garage!");
							continue;
						}
						// assign role
						switch (role) {
						case "em":
							isEmployee = true;
						case "cu": // case "em" is supposed to fall into "cu" (no break statement)
							sendMessage(MessageType.Success, "init:assigned_"+role);
							return;
						}
					} else {
						sendMessage(MessageType.Fail, "init:invalid");
						log.append(LogType.ERROR, client+" sent invalid init message!");
					}
				} catch (ClassNotFoundException | IOException e) {
					sendMessage(MessageType.Fail, "init:unknown");
					log.append(LogType.ERROR, e+" while listening for init message");
				}
			}
		}
		
		private boolean isValidInit(Message msg) { // checks if message is valid init message
			if (msg.getType() != MessageType.Request) { // init message must be request type
				return false;
			}
			String split[] = msg.getText().split(":");
			if (split.length != 3) { // init message must be formatted like this: "init:GA0:em"
				return false;
			}
			String command = split[0];
			String role = split[2];
			if (!command.equalsIgnoreCase("init")) {
				return false;
			}
			// server will find out if garage ID is valid later
			if (!role.equalsIgnoreCase("em") && !role.equalsIgnoreCase("cu")) { // if specified role is neither employee nor customer
				return false;
			}
			return true;
		}
		
		private void listenLogin() { // li; retrieves employee object after successful login
			
		}
		
		private boolean isRequestMsg(Message message) {
			return (message.getType() == MessageType.Request);
		}
		
		private boolean isLogoutMsg(Message message) {
			return (message.getText().equalsIgnoreCase("lo"));
		}
		
		public String[] getMessageParameters(Message message) {
			return message.getText().split(":");
		}
		
		public void runEmployeeCommand(String parameters[]) {
			try {
				String command = parameters[0];
				switch (command) {
				case "mp":
					String newPassword = parameters[1]; 
					modifyPassword(newPassword);
					break;
				case "mg":
					Double newGateTime = Double.parseDouble(parameters[1]);
					modifyGateTime(newGateTime);
					break;
				case "vr":
					viewReport();
					break;
				case "mr":
					Double newRate = Double.parseDouble(parameters[1]);
					modifyRate(newRate);
					break;
				case "vv":
					viewActiveTickets();
					break;
				case "vc":
					viewCameraList();
					break;
				case "vf":
					String cameraID = parameters[1];
					viewFeed(cameraID);
					break;
				default:
					runCustomerCommand(parameters); // roll into customer commands (common commands) if code doesn't match employee's
				}
			} catch (Exception e) {
				sendMessage(MessageType.Fail, "invalid_parameters");
			}
		}
		
		public void runCustomerCommand(String parameters[]) {
			try {
				String command = parameters[0];
				switch (command) {
				case "gt":
					generateTicket();
					break;
				case "va":
					viewAvailability();
					break;
				case "pt":
					String ticketID = parameters[1];
					payTicket(ticketID);
					break;
				case "tg":
					toggleGate();
					break;
				default:
					sendMessage(MessageType.Fail, "unknown_command");
					log.append(LogType.ERROR, client+" requested unknown command: "+command);
				}
			} catch (Exception e) {
				sendMessage(MessageType.Fail, "invalid_parameters");
				log.append(LogType.ERROR, client+" sent invalid message parameters!");
			}
		}
		
		private void handleEmployee() {
			listenLogin(); // authenticate employee's login credentials before reading other messages
			Message incoming = null;
			do {
				try {
					incoming = (Message)in.readObject();
					if (!isRequestMsg(incoming)) {
						continue; // ignore non-request messages
					}
					String parameters[] = getMessageParameters(incoming);
					runEmployeeCommand(parameters);
				} catch (Exception e) {
					sendMessage(MessageType.Fail, "unknown_error");
					log.append(LogType.ERROR, "Unable to process message from client "+client);
				}
			} while (!isLogoutMsg(incoming)); // end loop if client requests to logout
		}

		private void handleCustomer() {
			Message incoming = null;
			do {
				try {
					incoming = (Message)in.readObject();
					if (!isRequestMsg(incoming)) {
						continue; // ignore non-request messages
					}
					String parameters[] = getMessageParameters(incoming);
					runCustomerCommand(parameters);
				} catch (Exception e) {
					sendMessage(MessageType.Fail, "unknown_error");
					log.append(LogType.ERROR, "Unable to process message from client "+client);
				}
				
			} while (!isLogoutMsg(incoming)); // end loop if client requests to logout
		}
		
		// customer commands (includes common user commands)
		
		private void generateTicket() { // gt
			String ticketID = garage.generateTicket(); // generated ticket ID
			if (ticketID == null) {
				// TODO: error messages
				return;
			}
			// save new ticket to database
			Ticket ticket = garage.getTicket(ticketID);
			serverData.saveTicket(ticket);
			// success message
		}
		
		private void payTicket(String ticketID) { // pt
			Ticket ticket = serverData.getTicket(ticketID);
			if (ticketID == null) {
				// TODO: error messages
				return;
			}
			// update ticket
			ticket.pay();
			serverData.saveTicket(ticket);
			// success message
		}
		
		private void toggleGate() { // tg (customer guis send tg requests automatically)
			// command logic
			Gate gate = garage.getGate();
			if (gate.isOpen()) {
				gate.close(); // TODO: call GateHandler instead
			} else {
				gate.open();
			}
			// success message
		}
		
		private void viewAvailability() { // va
			int availabileSpaces = garage.getAvailableSpaces();
			// success message
			sendMessage(MessageType.Success, "va:"+availabileSpaces);
			log.append(LogType.ACTION, "Sent available spaces to client "+client);
		}
		
		// employee commands
		
		private void modifyPassword(String newPassword) { // mp
			// input validation
			if (!isValidPassword(newPassword)) {
				sendMessage(MessageType.Fail, "mp:no_special_characters");
				log.append(LogType.ERROR, "Unable to update "+employee.getID()+"'s password: needs special characters");
				return;
			}
			// command logic
			employee.setPassword(newPassword); // update password
			serverData.saveEmployee(employee); // update employee file with new password
			// success message
			sendMessage(MessageType.Success, "mp:password_changed");
			log.append(LogType.ACTION, employee.getID()+" has updated their password.");
		}
		
		private void modifyGateTime(double newOpenTime) { // mg
			// input validation
			// command logic
			garage.getGate().setOpenTime(newOpenTime);
			serverData.saveGarage(garage); // save garage parameters with new gate opening time
			// success message
		}
		
		private void overrideTicket(String ticketID, double newFee) { // ot
			// input validation
			if (newFee < 0) {
				// TODO: error messages
				return;
			}
			Ticket ticket = serverData.getTicket(ticketID);
			if (ticket == null) {
				// TODO: error messages
				return;
			}
			// command logic
			ticket.overrideFee(newFee);
			serverData.saveTicket(ticket);
			// success message
		}
		
		private void viewReport() { // vr
			Report report = garage.viewReport();
			serverData.saveReport(report); // save newly-updated report
			// return report as string
		}
		
		private void modifyRate(double newRate) { // mr
			// input validation
			if (newRate < 0) {
				// TODO: error messages
				return;
			}
			// update garage's rate
			garage.setHourlyRate(newRate);
			serverData.saveGarage(garage);
			// success message
		}
		
		private void viewActiveTickets() { // vv
			String ticketIDs = "";
			for (Ticket ticket : garage.getActiveTickets()) {
				ticketIDs += ticket.getID()+",";
			}
			// return ticketIDs string
		}
		
		private void viewCameraList() { // vc
			String cameraIDs = "";
			for (SecurityCamera camera : garage.getCameras()) {
				cameraIDs += camera.getID()+",";
			}
			// return cameraIDs string
		}
		
		private void viewFeed(String cameraID) { // vf
			// find security camera from id
			SecurityCamera camera = serverData.getSecurityCamera(cameraID);
			if (camera == null) {
				// TODO: error messages
				return;
			}
			// return live feed in a message
		}
		
		
	}
}
