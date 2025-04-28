package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import mock.Employee;
import mock.Garage;
import mock.Report;
import mock.SecurityCamera;
import mock.Ticket;

public class ServerData {
	private final String GARAGE_SUBDIR = "/garages/"; // "GA#.txt"
	private final String TICKET_SUBDIR = "/tickets/"; // "TI#.txt"
	private final String REPORT_SUBDIR = "/reports/"; // "GA#.txt"
	private final String CAMERA_SUBDIR = "/cameras/"; // "SC#.txt"
	private final String EMPLOYEE_SUBDIR = "/employees/"; // "EM#.txt"
	private String rootDir;
	private final Log log;
	
	// loaded server data
	// hash tables for efficiency (sorry!)
	private HashMap<String, Garage> garages = new HashMap<>(); // ID, object
	private HashMap<String, Ticket> tickets = new HashMap<>();
	private HashMap<String, Report> reports = new HashMap<>();
	private HashMap<String, SecurityCamera> cameras = new HashMap<>();
	private HashMap<String, Employee> employees = new HashMap<>();
	
	public ServerData(String rootDir, Log log) {
		this.log = log;
		assignRoot(rootDir);
		initFolders();
	}
	
	public void loadAll() { // loads data from all subdirectories
		loadGarages();
		loadTickets();
		loadReports();
		loadCameras();
		loadEmployees();
	}
	
	public Garage getGarage(String garageID) {
		if (garages.containsKey(garageID)) {
			return garages.get(garageID);
		}
		return null;
	}
	
	public Ticket getTicket(String ticketID) {
		if (tickets.containsKey(ticketID)) {
			return tickets.get(ticketID);
		}
		return null;
	}
	
	public Report getReport(String reportID) {
		if (reports.containsKey(reportID)) {
			return reports.get(reportID);
		}
		return null;
	}
	
	public SecurityCamera getSecurityCamera(String cameraID) {
		if (cameras.containsKey(cameraID)) {
			return cameras.get(cameraID);
		}
		return null;
	}
	
	public Employee getEmployee(String employeeID) {
		if (employees.containsKey(employeeID)) {
			return employees.get(employeeID);
		}
		return null;
	}
	
	// helper methods
	private void loadGarages() {
	    File garageDir = new File(Paths.get(rootDir, GARAGE_SUBDIR).toString());
	    for (File garageFile : garageDir.listFiles()) {
	        try (Scanner lineScanner = new Scanner(garageFile)) {
	            if (!lineScanner.hasNextLine()) { // if file is empty
	            	log.append(LogType.ERROR, "Skipping loading: " + garageFile.getName() + " (Empty data file)");
	            	continue; // skip it
	            }
	            // split parameters from string
	            String data = lineScanner.nextLine();
	            String[] split = data.split(",");
	            if (split.length != 4) {
	                log.append(LogType.ERROR, "Skipping loading: " + garageFile.getName() + " (Invalid number of parameters)");
	                continue;
	            }
	            // attempt to parse garage data
	            try {
	            	// garage parameters
	                String name = split[0];
	                double hourlyRate = Double.parseDouble(split[1]);
	                int capacity = Integer.parseInt(split[2]);
	                int gateOpenTime = Integer.parseInt(split[3]);
	                // instantiate garage object
	                Garage garage = new Garage(name, hourlyRate, capacity, gateOpenTime);
	                garages.put(garage.getID(), garage); // get assigned garage id and add garage to record
	            } catch (NumberFormatException e) {
	                log.append(LogType.ERROR, "Skipping loading: " + garageFile.getName() + " (Invalid data)");
	            }
	        // log error message if garage file was not found all of a sudden
	        } catch (FileNotFoundException e) {
	            log.append(LogType.ERROR, "Skipping loading: " + garageFile.getName() + " (File not found)");
	        }
	    }
	}
	
	private void loadTickets() {
		
	}
	
	private void loadReports() {
		
	}
	
	private void loadCameras() {
		
	}
	
	private void loadEmployees() {
		
	}
	
	private void assignRoot(String rootDir) {
		if (rootDir == null) { // if no root directory has been specified
			rootDir = ""; // leave it blank
		}
		this.rootDir = Paths.get(System.getProperty("user.dir"), rootDir).toString(); // full path
	}
	
	private void initFolders() { // creates directories if they don't already exist
		// check if root directory exists
		File root = new File(rootDir);
		if (!root.exists()) {
			root.mkdir(); // make new folder if it doesn't exist
		}
		// check subdirectories
		String subdirs[] = {GARAGE_SUBDIR, TICKET_SUBDIR, REPORT_SUBDIR, CAMERA_SUBDIR, EMPLOYEE_SUBDIR};
		for (String subdir : subdirs) {
			File sd = new File(Paths.get(rootDir, subdir).toString());
			if (!sd.exists()) {
				sd.mkdir(); // make new folder if it doesn't exist
			}
		}
	}
	
}
