package server;

import java.net.*;
import java.nio.file.Paths;

public class Server implements Runnable {
	// parameters
	private int port;
	private String logPrefix;
	private String logsDir;
	private String dataDir;
	// global attributes
	private boolean isRunning = false;
	private boolean isReady = false;
	private ServerSocket serverSocket;
	private ServerData serverData;
	private Log log;
	private boolean allowDataSaving;

	public Server(int port, String logPrefix, String logsDir, String dataDir, boolean allowDataSaving) {
		this.port = port;
		this.logPrefix = logPrefix;
		this.logsDir = logsDir;
		this.dataDir = dataDir;
		this.allowDataSaving = allowDataSaving;
	}
	
	public void run() {
		isRunning = true; // set "currently running" flag to true for client reception loop to run
		log = new Log(Paths.get(System.getProperty("user.dir"), logsDir).toString(), logPrefix); // create new log for server instance
		// load server data from file
		serverData = new ServerData(dataDir, log, allowDataSaving);
		serverData.loadAll();
		// starting connection
		try (ServerSocket temp = new ServerSocket(port)) {
			serverSocket = temp;
			serverSocket.setReuseAddress(true); // ensures server uses same ip address
			log.append("Server started at IP: "+serverSocket.getInetAddress().getHostAddress());
			isReady = true; // indicate that server is ready
			// client reception loop
			while (isRunning) {
				Socket client = serverSocket.accept();
				ClientHandler ch = new ClientHandler(client, log, serverData);
				new Thread(ch).start(); // client gets serviced in new thread
			}
		// exception handling
		} catch (Exception e) {
			if (isRunning) { // exceptions are expected on shutdown, so don't log when that happens
				log.append(LogType.ERROR, e+" in client reception"); // log error message to file
			}
		// when the server terminates
		} finally {
			stop();
		}
	}

	public void stop() { // stop procedure when terminating server
		log.append("Shutting down server...");
		isRunning = false; // set "currently running" flag to false to stop client reception loop
		closeServerSocket();
		serverData.saveAll(); // save all data
	}

	public void waitUntilReady() { // useful for programs waiting until server is ready to talk
		while (!isReady) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				continue;
			}
		}
	}

	// helper methods
	private void closeServerSocket() {
		try {
			serverSocket.close();
		} catch (Exception e) {
			log.append(LogType.ERROR, "Unable to close server connection");
		}
	}
}
