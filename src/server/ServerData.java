package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import mock.Earning;
import mock.Employee;
import mock.Garage;
import mock.Report;
import mock.SecurityCamera;
import mock.Ticket;

public class ServerData {
	private final String GARAGE_SUBDIR = "/garages/"; // "GA#.txt"
	private final String TICKET_SUBDIR = "/tickets/"; // "TI#.txt"
	private final String REPORT_SUBDIR = "/reports/"; // "RE#.txt"
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
	
	public void loadAll() {
		loadGarages();
		loadTickets();
		loadReports();
		loadCameras();
		loadEmployees();
	}
	
	public void saveAll() { // useful for when server is about to terminate
		
	}
	
	// methods for retrieving an object based on their id
	
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
	
	// methods to save an individual object to file
	
	public void saveGarage(Garage garage) { 
		
	}
	
	public void saveTicket(Ticket ticket) {
		
	}
	
	public void saveReport(Report report) {
		
	}
	
	public void saveSecurityCamera(SecurityCamera camera) {
		
	}
	
	public void saveEmployee(Employee employee) {
		
	}
	
	// helper methods
	private void loadGarages() {
		File dir = new File(getFullSubdir(GARAGE_SUBDIR));
		Scanner lineScanner;
		for (File curFile : dir.listFiles()) {
			try {
				lineScanner = new Scanner(curFile);
			} catch (FileNotFoundException e) {
				continue; // skip if file cannot be found all of a sudden
			}
			if (!lineScanner.hasNextLine()) continue; // skip if file is empty
			String curData = lineScanner.nextLine();
			lineScanner.close(); // close current line scanner instance
			if (!isValidGarageData(curData)) {
				continue; // skip if data is invalid
			}
			// parsing into object
			String split[] = curData.split(",");
			String name = split[0];
            double hourlyRate = Double.parseDouble(split[1]);
            int capacity = Integer.parseInt(split[2]);
            double gateOpenTime = Double.parseDouble(split[3]);
            // add garage to database
            Garage curGarage = new Garage(name, hourlyRate, capacity, gateOpenTime);
            garages.put(curGarage.getID(), curGarage);
		}
	}
	
	private void loadTickets() {
		File dir = new File(getFullSubdir(TICKET_SUBDIR));
		Scanner lineScanner;
		for (File curFile : dir.listFiles()) {
			try {
				lineScanner = new Scanner(curFile);
			} catch (FileNotFoundException e) {
				continue; // skip if file cannot be found all of a sudden
			}
			if (!lineScanner.hasNextLine()) continue; // skip if file is empty
			String curData = lineScanner.nextLine();
			lineScanner.close(); // close current line scanner instance
			if (!isValidTicketData(curData)) {
				continue; // skip if data is invalid
			}
			// parsing into object
			String split[] = curData.split(",");
			String garageID = split[0];
			Long entryTime = Long.parseLong(split[1]);
			Long exitTime = Long.parseLong(split[2]);
			boolean overridden = Boolean.parseBoolean(split[3]);
			boolean paid = Boolean.parseBoolean(split[4]);
			double fee = Double.parseDouble(split[5]);
			Garage garage = getGarage(garageID);;
			// check if associated garage exists in database
			if (garage == null) {
				continue; // skip loading ticket if it doesn't
			}
			// add ticket to database
			Ticket curTicket = new Ticket(garage, new Date(entryTime), new Date(exitTime), overridden, paid, fee);
			tickets.put(curTicket.getID(), curTicket);
			// add ticket to garage
			curTicket.getGarage().loadTicket(curTicket);
		}
	}
	
	private void loadReports() {
		File dir = new File(getFullSubdir(REPORT_SUBDIR));
		Scanner lineScanner;
		for (File curFile : dir.listFiles()) {
			try {
				lineScanner = new Scanner(curFile);
			} catch (FileNotFoundException e) {
				continue; // skip if file cannot be found all of a sudden
			}
			if (!lineScanner.hasNextLine()) continue; // check if all 3 lines exist
			String garageID = lineScanner.nextLine();
			if (!lineScanner.hasNextLine()) continue;
			String entriesStr = lineScanner.nextLine();
			if (!lineScanner.hasNextLine()) continue;
			String earningsStr = lineScanner.nextLine();
			lineScanner.close();
			// check if associated garage exists in database
			Garage garage = getGarage(garageID);
			if (garage == null) {
				continue; // skip loading ticket if it doesn't
			}
			// parsing into object
			Report curReport = new Report(garage);
			// load entry dates
			String entriesSplit[] = entriesStr.split(",");
			for (String curEntry : entriesSplit) {
				Long curEntryTimestamp = null;
				try { // attempt typecast conversion
					curEntryTimestamp = Long.parseLong(curEntry);
				} catch (Exception e) {
					continue; // skip entry if conversion failed
				}
				curReport.addEntryTime(new Date(curEntryTimestamp));
			}
			// load earnings
			String earningsSplit[] = earningsStr.split("\\|");
			for (String curEarningStr : earningsSplit) {
				String curEarningData[] = curEarningStr.split(",");
				if (curEarningData.length != 2) { // valid earnings have two parameters
					continue;
				}
				long curEarningTimestamp;
				double curEarningRevenue;
				try { // attempt typecast conversion
					curEarningTimestamp = Long.parseLong(curEarningData[0]);
					curEarningRevenue = Double.parseDouble(curEarningData[1]);
				} catch (Exception e) {
					continue; // skip earning if typecast failed
				}
				curReport.addExit(new Date(curEarningTimestamp), curEarningRevenue);
			}
			// add report to database
			reports.put(curReport.getID(), curReport);
			// add report to garage
			garage.loadReport(curReport);
		}
	}
	
	private void loadCameras() {
		File dir = new File(getFullSubdir(CAMERA_SUBDIR));
		Scanner lineScanner;
		for (File curFile : dir.listFiles()) {
			try {
				lineScanner = new Scanner(curFile);
			} catch (FileNotFoundException e) {
				continue; // skip if file cannot be found all of a sudden
			}
			if (!lineScanner.hasNextLine()) continue; // skip if file is empty
			String curData = lineScanner.nextLine();
			lineScanner.close(); // close current line scanner instance
			if (!isValidSecurityCameraData(curData)) {
				continue; // skip if data is invalid
			}
			Garage garage = getGarage(curData); // camera data only has 1 parameter (garage ID)
			// check if associated garage exists in database
			if (garage == null) {
				continue; // skip loading camera if it doesn't
			}
			// add camera to database
			SecurityCamera curCamera = new SecurityCamera(garage);
			cameras.put(curCamera.getID(), curCamera);
			// add camera to garage
			garage.addCamera(curCamera);
		}
	}
	
	private void loadEmployees() {
		File dir = new File(getFullSubdir(EMPLOYEE_SUBDIR));
		Scanner lineScanner;
		for (File curFile : dir.listFiles()) {
			try {
				lineScanner = new Scanner(curFile);
			} catch (FileNotFoundException e) {
				continue; // skip if file cannot be found all of a sudden
			}
			if (!lineScanner.hasNextLine()) continue; // skip if file is empty
			String curData = lineScanner.nextLine();
			lineScanner.close(); // close current line scanner instance
			if (!isValidEmployeeData(curData)) {
				continue; // skip if data is invalid
			}
			// parsing into object
			String split[] = curData.split(",");
			String garageID = split[0];
			String username = split[1];
			String password = split[2];
			Garage garage = garages.get(garageID);
			// check if associated garage exists in database
			if (garage == null) {
				continue; // skip loading employee if it doesn't
			}
			// add employee to database
			Employee curEmployee = new Employee(garage, username, password);
			employees.put(curEmployee.getID(), curEmployee);
		}
	}
	
	private String getFullSubdir(String subdir) { // returns full subdirectory path
		return Paths.get(rootDir, subdir).toString();
	}
	
	private boolean isValidGarageData(String garageData) {
		String split[] = garageData.split(",");
		if (split.length != 4) { // valid garages have 4 parameters
			return false;
		}
		//String name; // no need to check name
        double hourlyRate;
        int capacity;
        double gateOpenTime;
		try { // typecast conversion check
			//name = split[0];
            hourlyRate = Double.parseDouble(split[1]);
            capacity = Integer.parseInt(split[2]);
            gateOpenTime = Double.parseDouble(split[3]);
		} catch (Exception e) {
			return false;
		}
		if (hourlyRate < 0) { // hourly rate cannot be negative
			return false;
		}
		if (capacity < 0) { // capacity cannot be negative
			return false;
		}
		if (gateOpenTime < 0) { // gate's open time cannot be negative
			return false;
		}
		return true;
	}
	
	private boolean isValidTicketData(String ticketData) {
		String split[] = ticketData.split(",");
		if (split.length != 6) { // valid tickets have 6 parameters
			return false;
		}
		//String garageID;
		@SuppressWarnings("unused")
		Long entryTime;
		@SuppressWarnings("unused")
		Long exitTime;
		@SuppressWarnings("unused")
		boolean overridden;
		@SuppressWarnings("unused")
		boolean paid;
		double fee;
		try { // typecast conversion check
			//garageID = split[0];
			entryTime = Long.parseLong(split[1]);
			exitTime = Long.parseLong(split[2]);
			overridden = Boolean.parseBoolean(split[3]);
			paid = Boolean.parseBoolean(split[4]);
			fee = Double.parseDouble(split[5]);
		} catch (Exception e) {
			return false;
		}
		if (fee < 0) { // fees cannot be negative
			return false;
		}
		return true;
	}
	
	// private boolean isValidReportData(String reportData) // reports will be checked in their loading method
	
	private boolean isValidSecurityCameraData(String cameraData) {
		String split[] = cameraData.split(",");
		if (split.length != 1) { // valid cameras only have 1 parameter
			return false;
		}
		//String garageID = split[0];
		return true;
	}
	
	private boolean isValidEmployeeData(String employeeData) {
		String split[] = employeeData.split(",");
		if (split.length != 3) { // valid employees have 3 parameters
			return false;
		}
		//String garageID = split[0];
		//String username = split[1];
		//String password = split[2]; // main server logic will handle password validation
		return true;
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
