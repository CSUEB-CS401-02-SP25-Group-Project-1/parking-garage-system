package server;

public class RunServer {
	public static void main(String[] args) {
		// default server parameters
		int port = 7777;
		String logPrefix = "server_";
		String logsDir = "/logs/";
		String dataDir = "/data/";
		boolean allowDataSaving = true;
		// replace parameters if there's arguments
		if (args.length == 5) {
			port = Integer.parseInt(args[0]);
			logPrefix = args[1];
			logsDir = args[2];
			dataDir = args[3];
			allowDataSaving = Boolean.parseBoolean(args[4]);
		}
		// start server
		new Thread(new Server(port, logPrefix, logsDir, dataDir, allowDataSaving)).start();
	}
}
