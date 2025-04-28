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
	
	public static void main(String[] args) {
		Log log = new Log(Paths.get(System.getProperty("user.dir"), LOGS_DIR).toString(), LOG_PREFIX); // create new log for server instance
		// TODO: some business logic here for loading server components from file
		// starting connection
		try (ServerSocket ss = new ServerSocket(PORT)) {
			ss.setReuseAddress(true); // ensures server uses same ip address
			log.append("Server started at IP: "+ss.getInetAddress().getHostAddress());
			// server loop
			while (true) {
				Socket client = ss.accept();
				ClientHandler ch = new ClientHandler(client, log);
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
		private Garage garage;
		ObjectOutputStream out; // outgoing messages to client
		ObjectInputStream in; // incoming messages from client
		
		public ClientHandler(Socket client, Log log) {
			this.client = client;
			this.log = log;
		}
		
		public void run() {
			log.append(client.getInetAddress().getHostAddress()+" has connected"); // TODO: maybe include client id/type too?
			try {
				out = new ObjectOutputStream(client.getOutputStream());
				in = new ObjectInputStream(client.getInputStream());
				// processes init message (assigns client garage and role)
				// login loop (for employee client)
				// main communication loop
			} catch (IOException e) {
				log.append(LogType.ERROR, e+" in client communication"); // TODO: identify which client caused this error (ip or some other id)
			}
		}
		
		// helper methods
		private boolean isLoggingOut(Message msg) { // determines if the client is requesting to log out
			return false; // TODO: write code here
		}
	}
}
