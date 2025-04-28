package server;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import mock.Garage;
import mock.Report;
import mock.SecurityCamera;
import mock.Ticket;

public class ServerData {
	private final String GARAGE_SUBDIR = "/garages/"; // "GA#.txt"
	private final String TICKET_SUBDIR = "/tickets/"; // "TI#.txt"
	private final String REPORT_SUBDIR = "/reports/"; // "GA#.txt"
	private final String CAMERA_SUBDIR = "/cameras/"; // "SC#.txt"
	private String rootDir;
	
	// loaded server data
	// hash tables for efficiency (sorry!)
	private HashMap<String, Garage> garages = new HashMap<>(); // ID, object
	private HashMap<String, Ticket> tickets = new HashMap<>();
	private HashMap<String, Report> reports = new HashMap<>();
	private HashMap<String, SecurityCamera> cameras = new HashMap<>();
	
	public ServerData(String rootDir) {
		assignRoot(rootDir);
		initFolders();
	}
	
	public loadAll() { // loads data from all subdirectories
		loadGarages();
		loadTickets();
		loadReports();
		loadCameras();
	}
	
	// helper methods
	private void loadGarages() {
		
	}
	
	private void assignRoot(String rootDir) {
		if (rootDir == null) { // if no root directory has been specified
			rootDir = System.getProperty("user.dir"); // use working directory
		}
		this.rootDir = rootDir;
	}
	
	private void initFolders() { // creates directories if they don't already exist
		// check if root directory exists
		File root = new File(rootDir);
		if (!root.exists()) {
			root.mkdir(); // make new folder if it doesn't exist
		}
		// check subdirectories
		String subdirs[] = {GARAGE_SUBDIR, TICKET_SUBDIR, REPORT_SUBDIR, CAMERA_SUBDIR};
		for (String subdir : subdirs) {
			File sd = new File(Paths.get(rootDir, subdir).toString());
			if (!sd.exists()) {
				sd.mkdir(); // make new folder if it doesn't exist
			}
		}
	}
	
}
