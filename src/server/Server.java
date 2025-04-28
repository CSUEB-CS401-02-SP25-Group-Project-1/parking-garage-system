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
		ServerData serverData = new ServerData(DATA_DIR);
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
	
	public static Garage getGarage(String garageID) {
		
	}
	
	private static class ClientHandler implements Runnable {
		private final Socket client;
		private final Log log;
		private boolean isEmployee;
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
		
		private void listenLogin() {
			
		}
		
		private void handleEmployee() {
			listenLogin(); // authenticate employee's login credentials before reading other messages
		}

		private void handleCustomer() {
			
		}
		
		
	}
}
