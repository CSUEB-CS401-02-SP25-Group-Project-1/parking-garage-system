package server;

import mock.*; // TODO: temp
import shared.*;
import java.io.IOException;
import java.net.*;

public class Server {
	final static int PORT = 7777; // server port number
	public static void main(String[] args) {
		// TODO: some business logic here for loading server components from file
		try (ServerSocket ss = new ServerSocket(PORT)) {
			ss.setReuseAddress(true); // ensures server uses same ip address
			System.out.println("Server started at IP: "+ss.getInetAddress().getHostAddress()); // TODO: replace all print statements with logs
			while (true) { // server loop
				Socket client = ss.accept();
				ClientHandler ch = new ClientHandler(client);
				new Thread(ch).start(); // client is handled in new thread
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class ClientHandler implements Runnable {
		private final Socket client;
		
		public ClientHandler(Socket client) {
			this.client = client;
		}
		
		public void run() {
			System.out.println(client.getInetAddress().getHostAddress()+" has connected"); // TODO: temp
		}
	}
}
