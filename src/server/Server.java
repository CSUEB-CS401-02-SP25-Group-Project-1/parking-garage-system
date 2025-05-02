package server;

import server.*; // TODO: temp
import shared.*;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import javax.swing.ImageIcon;

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
		} catch (Exception e) {
			log.append(LogType.ERROR, e+" in client reception"); // log error message to file
		} finally {
			serverData.saveAll(); // save all data when terminating server
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
			log.append(client +" has connected");
			try {
				out = new ObjectOutputStream(client.getOutputStream());
				in = new ObjectInputStream(client.getInputStream());
				listenInit(); // listen for init message
				if (isEmployee) { // if listenInit() found employee role
					handleEmployee(); // handle client as employee
				} else { 
					handleCustomer(); // handle client as customer instead
				}
			} catch (Exception e) {
				log.append(LogType.ERROR, e+" when communicating with client "+client);
			} finally {
				sendMessage(MessageType.Success, "lo:signed_out");
				try {
					out.close();
					in.close();
					client.close();
					log.append(LogType.ACTION, client+" has logged out", garage);
				} catch (Exception e) {
					log.append(LogType.ERROR, e+" when singing out client "+client);
				}
				
			}
		}
		
		// helper methods
		private void sendMessage(MessageType type, String text) {
			try {
				out.writeObject(new Message(type, "server", text));
			} catch (Exception e) {
				log.append(LogType.ERROR, e+" while attempting to send message to client ("+client+")");
			}
		}
		
		private void sendImageMessage(MessageType type, String text, ImageIcon image) { // for live security camera feed
			try {
				out.writeObject(new ImageMessage(type, "server", text, image));
			} catch (Exception e) {
				log.append(LogType.ERROR, e+" while attempting to send image message to client ("+client+")");
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
				} catch (Exception e) {
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
			while (true) {
				Message incoming;
				try {
					incoming = (Message)in.readObject();
					if (!isRequestMsg(incoming)) {
						continue; // ignore non-request messages
					}
					String parameters[] = incoming.getText().split(":");
					String command = parameters[0];
					String username = parameters[1];
					String password = parameters[2];
					if (!command.equalsIgnoreCase("li")) {
						continue; // reject non-login requests
					}
					if (!isValidCredentials(username, password)) {
						sendMessage(MessageType.Fail, "li:invalid_credentials");
						log.append(LogType.ERROR, "Client "+client+" submitted invalid login credentials!");
						continue; // deny invalid credentials
					}
					// successful login
					employee = serverData.getEmployeeByUsername(username); // associate employee account with connection
					sendMessage(MessageType.Success, "li:successful");
					log.append(LogType.ACTION, "Employee "+employee.getUsername()+" logged in at "+client, garage);
					return;
				} catch (Exception e) {
					sendMessage(MessageType.Fail, "li:unknown_error");
					log.append(LogType.ERROR, e+": Unable to process login request for client "+client);
				}
			}
		}
		
		private boolean isValidCredentials(String username, String password) {
			Employee employee = serverData.getEmployeeByUsername(username);
			if (employee == null) {
				return false;
			}
			if (!employee.getPassword().equals(password)) {
				return false;
			}
			return true;
		}
		
		private boolean isRequestMsg(Message message) {
			return (message.getType() == MessageType.Request);
		}
		
		private boolean isLogoutMsg(Message message) {
			return (message.getText().equalsIgnoreCase("lo"));
		}
		
		private String[] getMessageParameters(Message message) {
			return message.getText().split(":");
		}
		
		private boolean isValidPassword(String password) {
			return password.matches(".*[^a-zA-Z0-9].*"); // checks if password contains at least one special character
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
				case "ot":
					String ticketID = parameters[1];
					Double newFee = Double.parseDouble(parameters[2]);
					overrideTicket(ticketID, newFee);
					break;
				case "vl":
					viewLogs();
					break;
				default:
					runCustomerCommand(parameters); // roll into customer commands (common commands) if code doesn't match employee's
				}
			} catch (Exception e) {
				sendMessage(MessageType.Fail, "invalid_parameters");
				log.append(LogType.ERROR, client+" sent invalid message parameters!");
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
					float payment = Float.parseFloat(parameters[2]);
					payTicket(ticketID, payment);
					break;
				case "tg":
					toggleGate();
					break;
				case "bs":
					ticketID = parameters[1];
					viewBillingSummary(ticketID);
					break;
				case "gn":
					viewGarageName();
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
			while (true) {
				try {
					incoming = (Message)in.readObject();
					if (isLogoutMsg(incoming)) { 
						break; // end loop if client requests to logout
					}
					if (!isRequestMsg(incoming)) {
						continue; // ignore non-request messages
					}
					String parameters[] = getMessageParameters(incoming);
					runEmployeeCommand(parameters);
				} catch (Exception e) {
					sendMessage(MessageType.Fail, "unknown_error");
					log.append(LogType.ERROR, "Unable to process message from client "+client);
				}
			} 
		}

		private void handleCustomer() {
			Message incoming = null;
			while (true) {
				try {
					incoming = (Message)in.readObject();
					if (isLogoutMsg(incoming)) { 
						break; // end loop if client requests to logout
					}
					if (!isRequestMsg(incoming)) {
						continue; // ignore non-request messages
					}
					String parameters[] = getMessageParameters(incoming);
					runCustomerCommand(parameters);
				} catch (Exception e) {
					sendMessage(MessageType.Fail, "unknown_error");
					log.append(LogType.ERROR, "Unable to process message from client "+client);
				}
				
			}
		}
		
		// customer commands (includes common user commands)
		
		private void generateTicket() { // gt
			String ticketID = garage.generateTicket(); // generated ticket ID
			if (ticketID == null) {
				sendMessage(MessageType.Fail, "gt:unavailable_space");
				log.append(LogType.ERROR, "Unable to generate ticket for "+client+ "(no space in garage "+garage.getID()+")", garage);
				return;
			}
			// save new ticket to database
			Ticket ticket = garage.getTicket(ticketID);
			serverData.saveTicket(ticket);
			// success message
			sendMessage(MessageType.Success, "gt:"+ticket.getID());
			log.append(LogType.ENTRY, "Generated ticket "+ticket.getID()+" for client "+client, garage);
		}
		
		private void payTicket(String ticketID, float amount) { // pt
			// input validation
			Ticket ticket = serverData.getTicket(ticketID);
			if (ticket == null) { // check if ticket exists in database
				sendMessage(MessageType.Fail, "pt:ticket_not_found");
				log.append(LogType.ERROR, "Unable to retrieve ticket "+ticketID+" for client "+client+ "(ticket not found)", garage);
				return;
			}
			if (ticket.isPaid()) { // check if ticket hasn't been paid for yet
				sendMessage(MessageType.Fail, "pt:already_paid");
				log.append(LogType.ERROR, "Unable to fulfill payment for ticket "+ticket.getID()+" for client "+client+ "(already paid)", garage);
				return;
			}
			// update ticket
			boolean success = ticket.pay(amount);
			if (!success) { // check if client has paid enough
				sendMessage(MessageType.Fail, "pt:insufficient_funds");
				log.append(LogType.ERROR, "Unable to fulfull payment for ticket "+ticket.getID()+" for client "+client+ "(insufficient funds)", garage);
			}
			serverData.saveTicket(ticket);
			// return receipt
			sendMessage(MessageType.Success, "pt:"+new Receipt(ticket.getID(), garage.getName(),
						ticket.getEntryTime(), ticket.getExitTime(), ticket.getFee())); 
						// clients will have to discern that this is a human-readable string after the "pt:" prefix
			log.append(LogType.ACTION, "Sent receipt of paid ticket "+ticket.getID()+" to client "+client, garage);
		}
		
		private void toggleGate() { // tg (customer guis send tg requests automatically)
			// command logic
			Gate gate = garage.getGate();
			GateHandler gh = new GateHandler(gate, log);
			new Thread(gh).start(); // gate logic will run in a new thread
			// success message
			sendMessage(MessageType.Success, "tg:toggled");
			log.append(LogType.ACTION, "Toggled gate for client "+client, garage);
		}
		
		private void viewAvailability() { // va
			int availabileSpaces = garage.getAvailableSpaces();
			// success message
			sendMessage(MessageType.Success, "va:"+availabileSpaces);
			log.append(LogType.ACTION, "Sent available spaces to client "+client);
		}
		
		private void viewBillingSummary(String ticketID) { // bs
			// input validation
			Ticket ticket = serverData.getTicket(ticketID);
			if (ticket == null) {
				sendMessage(MessageType.Fail, "bs:invalid_ticket_id");
				log.append(LogType.ERROR, "Unable to return billing summary for ticket "+ticketID+ " to client "+client);
				return;
			}
			// calculate fee in real time
			ticket.calculateFee();
			serverData.saveTicket(ticket); // save calculated fee to file
			// return ticket data with newly-calculated fee
			String payload = "bs:"+ticket.getID()+":"+ticket.getEntryTime()+":"+ticket.getExitTime()+":"+ticket.getFee();
			sendMessage(MessageType.Success, payload);
			log.append(LogType.ACTION, "Sent billing summary for ticket "+ticketID+" to client "+client);
		}
		
		private void viewGarageName() { // gn
			String name = garage.getName();
			// success message
			sendMessage(MessageType.Success, "gn:"+name); 
						// clients will have to discern this is a human-readable string after the "gn:" prefix
		}
		
		// employee commands
		
		private void modifyPassword(String newPassword) { // mp
			// input validation
			if (!isValidPassword(newPassword)) {
				sendMessage(MessageType.Fail, "mp:no_special_characters");
				log.append(LogType.ERROR, "Unable to update "+employee.getUsername()+"'s password (needs special characters)", garage);
				return;
			}
			// command logic
			employee.setPassword(newPassword); // update password
			serverData.saveEmployee(employee); // update employee file with new password
			// success message
			sendMessage(MessageType.Success, "mp:password_changed");
			log.append(LogType.ACTION, employee.getUsername()+" has updated their password", garage);
		}
		
		private void modifyGateTime(double newOpenTime) { // mg
			// input validation
			if (newOpenTime < 0) {
				sendMessage(MessageType.Fail, "mg:invalid_time");
				log.append(LogType.ERROR, "Unable to update garage "+garage.getID()+"'s gate opening time for employee "+employee.getUsername()+" (invalid time)", garage);
				return;
			}
			// command logic
			garage.getGate().setOpenTime(newOpenTime);
			serverData.saveGarage(garage); // save garage parameters with new gate opening time
			// success message
			sendMessage(MessageType.Success, "mg:time_updated");
			log.append(LogType.ACTION, employee.getUsername()+" has updated the gate opening time for garage "+garage.getID(), garage);
		}
		
		private void overrideTicket(String ticketID, double newFee) { // ot
			// input validation
			if (newFee < 0) {
				sendMessage(MessageType.Fail, "ot:invalid_fee");
				log.append(LogType.ERROR, "Unable to override ticket "+ticketID+" for employee "+employee.getUsername()+" (invalid value)", garage);
				return;
			}
			Ticket ticket = serverData.getTicket(ticketID);
			if (ticket == null) {
				sendMessage(MessageType.Fail, "ot:ticket_not_found");
				log.append(LogType.ERROR, "Unable to override ticket "+ticketID+" for employee "+employee.getUsername()+" (ticket not found)", garage);
				return;
			}
			// command logic
			ticket.overrideFee(newFee);
			serverData.saveTicket(ticket);
			// success message
			sendMessage(MessageType.Success, "ot:overridden_ticket");
			log.append(LogType.ACTION, employee.getUsername()+" has overridden ticket "+ticket.getID(), garage);
		}
		
		private void viewReport() { // vr
			Report report = garage.viewReport();
			serverData.saveReport(report); // save newly-updated report
			// return report as string
			sendMessage(MessageType.Success, 
						"vr:"+report.getRevenueThisHour()+","+report.getRevenueToday()+","+
						report.getRevenueThisWeek()+","+report.getRevenueThisMonth()+","+
						report.getRevenueToday()+","+report.getTotalRevenue()+","+
						report.getPeakHour()+","+report.getCurrentlyParkedNum());
			log.append(LogType.ACTION, "Generated garage report for employee "+employee.getUsername(), garage);
		}
		
		private void modifyRate(double newRate) { // mr
			// input validation
			if (newRate < 0) {
				sendMessage(MessageType.Fail, "mr:invalid_rate");
				log.append(LogType.ERROR, "Unable to modify hourly rate for garage "+garage.getID()+" for employee "+employee.getUsername()+" (invalid value)", garage);
				return;
			}
			// update garage's rate
			garage.setHourlyRate(newRate);
			serverData.saveGarage(garage); // save new rate to file
			// success message
			sendMessage(MessageType.Success, "mr:modified_rate");
			log.append(LogType.ACTION, "Employee "+employee.getUsername()+" updated hourly rate for garage: "+garage.getHourlyRate(), garage);
		}
		
		private void viewActiveTickets() { // vv
			String ticketIDs = "";
			for (Ticket ticket : garage.getActiveTickets()) {
				ticketIDs += ticket.getID()+",";
			}
			// return ticketIDs string in message
			sendMessage(MessageType.Success, "vv:"+ticketIDs);
			log.append(LogType.ACTION, "Sent list of active tickets in garage "+garage.getID()+" to employee "+employee.getUsername(), garage);
		}
		
		private void viewCameraList() { // vc
			String cameraIDs = "";
			for (SecurityCamera camera : garage.getCameras()) {
				cameraIDs += camera.getID()+",";
			}
			// return cameraIDs string
			sendMessage(MessageType.Success, "vc:"+cameraIDs);
			log.append(LogType.ACTION, "Sent list of cameras in garage "+garage.getID()+" to employee "+employee.getUsername(), garage);
		}
		
		private void viewFeed(String cameraID) { // vf
			// find security camera from id
			SecurityCamera camera = serverData.getSecurityCamera(cameraID);
			if (camera == null) {
				sendMessage(MessageType.Fail, "vf:camera_not_found");
				log.append(LogType.ERROR, "Unable to send live feed of camera "+cameraID+" to employee "+employee.getUsername()+" (camera not found)", garage);
				return;
			}
			// return live feed in an image message
			sendImageMessage(MessageType.Success, "vf:image", camera.view());
			log.append(LogType.ACTION, "Sent live feed of camera "+camera.getID()+" to employee "+employee.getUsername(), garage);
		}
		
		private void viewLogs() { // vl
			String logsAsString = "";
			for (String log : garage.getLogEntries()) {
				logsAsString += log+"\n";
			}
			// send garage logs in message payload
			sendMessage(MessageType.Success, "vl:"+logsAsString);
				// clients must discern that this is a human-readable string after the "vl:" prefix
			log.append(LogType.ACTION, "Sent current logs of garage "+garage.getID()+" to employee "+employee.getUsername(), garage);
		}
		
		private class GateHandler implements Runnable { // a class that keeps gate toggling logic running in another thread
			private final Gate gate;
			private final Log log;
			
			public GateHandler(Gate gate, Log log) {
				this.gate = gate;
				this.log = log;
			}
			
			public void run() {
				if (gate.isOpen()) {
					closeGate();
				} else {
					openGate();
				}
			}
			
			private void openGate() { // opens gate for a certain amount of time before automatically closing it
				gate.open();
				log.append(LogType.ACTION, "Gate is now opening in garage "+garage.getID(), garage);
				long millis = (long)(gate.getOpenTime() * 1000); // remains open for this amount of time before automatically closing
				try {
					Thread.sleep(millis); // wait
					closeGate();
				} catch (InterruptedException e) {
					log.append(LogType.ERROR, "Unable to close gate in garage "+garage.getID(), garage);
				} 
			}
			
			private void closeGate() {
				gate.close();
				log.append(LogType.ACTION, "Gate is now closing in garage "+garage.getID(), garage);
			}
		}
	}
}
