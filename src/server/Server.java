package server;

import mock.*; // TODO: temp
import shared.*;
import java.io.IOException;
import java.net.*;
import java.nio.file.Paths;

public class Server {
	final static int PORT = 7777; // server port number
	final static String LOGS_DIR = "/logs/"; // saved logs directory
	final static String LOG_PREFIX = "server_"; // log filename prefix
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
			e.printStackTrace();
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
			log.append(client.getInetAddress().getHostAddress()+" has connected"); // TODO: temp
		}
	}
}
