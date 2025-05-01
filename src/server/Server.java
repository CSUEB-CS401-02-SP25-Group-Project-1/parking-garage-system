package server;

import mock.*; // TODO: temp
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
	private static ServerData serverData;
	
	public static void main(String[] args) {
		Log log = new Log(Paths.get(System.getProperty("user.dir"), LOGS_DIR).toString(), LOG_PREFIX); // create new log for server instance
		// load server data from file
		serverData = new ServerData(DATA_DIR, log);
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
}
