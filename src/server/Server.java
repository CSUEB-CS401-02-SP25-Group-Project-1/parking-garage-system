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
		
		public ClientHandler(Socket client, Log log) {
			this.client = client;
			this.log = log;
		}
		
		public void run() {
			log.append(client.getInetAddress().getHostAddress()+" has connected"); // TODO: maybe include client id/type too?
			try {
				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream()); // outgoing messages to client
				ObjectInputStream in = new ObjectInputStream(client.getInputStream()); // incoming messages from client
				Message outgoing;
				Message incoming;
				// TODO: depending on the design, you may want to have server check if client wants to login first before processing any other message
				do { // client-server message communication loop
					incoming = (Message)in.readObject();
					log.append("Client says: "+incoming.getText()); // TODO: temp
					outgoing = new Message(MessageType.Success, "server", "foo"); // TODO: temp
					out.writeObject(outgoing); // sends server response to client
				} while (true); // TODO: change this to !isLoggingOut()
				//client.close();
				//log.append(client.getInetAddress().getHostAddress()+" has logged out"); // TODO: uncomment
			} catch (IOException | ClassNotFoundException e) {
				log.append(LogType.ERROR, e+" in client communication"); // TODO: identify which client caused this error (ip or some other id)
			}
		}
		
		// helper methods
		private boolean isLoggingOut(Message msg) { // determines if the client is requesting to log out
			return false; // TODO: write code here
		}
	}
}
