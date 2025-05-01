package server;

public class RunServer {
	public static void main(String[] args) {
		int port = 7777;
		String logPrefix = "server_";
		String logsDir = "/logs/";
		String dataDir = "/data";
		if (args.length == 4) {
			port = Integer.parseInt(args[0]);
			logPrefix = args[1];
			logsDir = args[2];
			dataDir = args[3];
		}
		new Thread(new Server(port, logPrefix, logsDir, dataDir)).run();
	}
}
