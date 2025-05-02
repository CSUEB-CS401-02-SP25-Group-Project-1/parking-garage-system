package server;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import mock.Earning;
import mock.Employee;
import mock.Garage;
import mock.Report;
import mock.Ticket;

public class ServerData {
	private final String GARAGE_SUBDIR = "/garages/"; // "GA#.txt"
	private final String TICKET_SUBDIR = "/tickets/"; // "TI#.txt"
	private final String REPORT_SUBDIR = "/reports/"; // "RE#.txt"
	private final String CAMERA_SUBDIR = "/cameras/"; // "SC#.txt"
	private final String EMPLOYEE_SUBDIR = "/employees/"; // "EM#.txt"
	private String rootDir;
	private final Log log;
	private boolean allowSaving; // used for debugging
	
	// loaded server data
	// hash tables for efficiency (sorry!)
	private HashMap<String, Garage> garages = new HashMap<>(); // ID, object
	private HashMap<String, Ticket> tickets = new HashMap<>();
	private HashMap<String, Report> reports = new HashMap<>();
	private HashMap<String, SecurityCamera> cameras = new HashMap<>();
	private HashMap<String, Employee> employees = new HashMap<>();
	private HashMap<String, Employee> employeesByUsername = new HashMap<>(); // used to search employees by username rather than by id
	
	public ServerData(String rootDir, Log log, boolean allowSaving) {
		this.log = log;
		determineSaving(allowSaving);
		assignRoot(rootDir);
		initFolders();
	}
	
	public void loadAll() {
		loadAllGarages();
		loadAllTickets();
		loadAllReports();
		loadAllCameras();
		loadAllEmployees();
	}
	
	public synchronized void saveAll() { // useful for when server is about to terminate
		if (allowSaving) {
			saveAllGarages();
			saveAllTickets();
			saveAllReports();
			saveAllCameras();
			saveAllEmployees();
		}
	}
	
	// methods for retrieving an object based on their id
	
	public synchronized Garage getGarage(String garageID) {
		if (garages.containsKey(garageID)) {
			return garages.get(garageID);
		}
		return null;
	}
	
	public synchronized Ticket getTicket(String ticketID) {
		if (tickets.containsKey(ticketID)) {
			return tickets.get(ticketID);
		}
		return null;
	}
	
	public synchronized Report getReport(String reportID) {
		if (reports.containsKey(reportID)) {
			return reports.get(reportID);
		}
		return null;
	}
	
	public synchronized SecurityCamera getSecurityCamera(String cameraID) {
		if (cameras.containsKey(cameraID)) {
			return cameras.get(cameraID);
		}
		return null;
	}
	
	public synchronized Employee getEmployee(String employeeID) {
		if (employees.containsKey(employeeID)) {
			return employees.get(employeeID);
		}
		return null;
	}
	
	public synchronized Employee getEmployeeByUsername(String garageID, String username) {
		String key = garageID+":"+username;
		if (employeesByUsername.containsKey(key)) {
			return employeesByUsername.get(key);
		}
		return null;
	}
	
	// methods to save an individual object to file 
	// (boolean to indicate success; return true if there's no errors)
	
	public synchronized boolean saveGarage(Garage garage) { 
		if (getGarage(garage.getID()) == null) {
	    	garages.put(garage.getID(), garage); // add garage to database if it's not there already
	    }
		if (!allowSaving) {
			return true; // don't save if saving is disabled
		}
		// actual saving logic
		String savePath = Paths.get(getFullSubdir(GARAGE_SUBDIR), garage.getID()+".txt").toString();
	    try (FileWriter saveFile = new FileWriter(savePath)) {
	        saveFile.write(garage.toString());
	    } catch (Exception e) {
	        log.append(LogType.ERROR, "Unable to save garage "+garage.getID()+" to file: "+e);
			return false;
	    }
		return true;
	}

	public synchronized boolean saveTicket(Ticket ticket) {
		if (getTicket(ticket.getID()) == null) {
	    	tickets.put(ticket.getID(), ticket); // add ticket to database if it's not there already
	    }
		if (!allowSaving) {
			return true; // don't save if saving is disabled
		}
		// actual saving logic
	    String savePath = Paths.get(getFullSubdir(TICKET_SUBDIR), ticket.getID()+".txt").toString();
	    try (FileWriter saveFile = new FileWriter(savePath)) {
	        saveFile.write(ticket.toString());
	    } catch (Exception e) {
	        log.append(LogType.ERROR, "Unable to save ticket "+ticket.getID()+" to file: "+e);
			return false;
	    }
		return true;
	}

	public synchronized boolean saveReport(Report report) {
		if (getReport(report.getID()) == null) {
	    	reports.put(report.getID(), report); // add report to database if it's not there already
	    }
		if (!allowSaving) {
			return true; // don't save if saving is disabled
		}
		// actual saving logic
	    String savePath = Paths.get(getFullSubdir(REPORT_SUBDIR), report.getID()+".txt").toString();
	    try (FileWriter saveFile = new FileWriter(savePath)) {
	        saveFile.write(report.toString());
	    } catch (Exception e) {
	        log.append(LogType.ERROR, "Unable to save report "+report.getID()+" to file: "+e);
			return false;
	    }
		return true;
	}

	public synchronized boolean saveSecurityCamera(SecurityCamera camera) {
		if (getSecurityCamera(camera.getID()) == null) {
	    	cameras.put(camera.getID(), camera); // add camera to database if it's not there already
	    }
		if (!allowSaving) {
			return true; // don't save if saving is disabled
		}
		// actual saving logic
	    String savePath = Paths.get(getFullSubdir(CAMERA_SUBDIR), camera.getID()+".txt").toString();
	    try (FileWriter saveFile = new FileWriter(savePath)) {
	        saveFile.write(camera.toString());
	    } catch (Exception e) {
	        log.append(LogType.ERROR, "Unable to save camera "+camera.getID()+" to file: "+e);
			return false;
	    }
		return true;
	}

	public synchronized boolean saveEmployee(Employee employee) {
		if (getEmployee(employee.getID()) == null) {
	    	addEmployeeToDatabases(employee); // add employee to database if it's not there already
	    }
		if (!allowSaving) {
			return true; // don't save if saving is disabled
		}
		// actual saving logic
	    String savePath = Paths.get(getFullSubdir(EMPLOYEE_SUBDIR), employee.getID()+".txt").toString();
	    try (FileWriter saveFile = new FileWriter(savePath)) {
	        saveFile.write(employee.toString());
	    } catch (Exception e) {
	        log.append(LogType.ERROR, "Unable to save employee "+employee.getID()+" to file: "+e);
			return false;
	    }
		return true;
	}
	
	// helper methods
	private void determineSaving(boolean allowSaving) {
		this.allowSaving = allowSaving;
		if (!this.allowSaving) {
			log.append(LogType.WARNING, "File saving is disabled! Server data will not be saved!");
		}
	}

	private synchronized void addEmployeeToDatabases(Employee employee) {
		employees.put(employee.getID(), employee);
		employeesByUsername.put(employee.getGarage().getID()+":"+employee.getUsername(), employee); // garageID:username
	}

	private void saveAllGarages() {
		for (String garageID : garages.keySet()) {
			Garage garage = garages.get(garageID);
			saveGarage(garage);
		}
	}
	
	private void saveAllTickets() {
		for (String ticketID : tickets.keySet()) {
			Ticket ticket = tickets.get(ticketID);
			saveTicket(ticket);
		}
	}
	
	private void saveAllReports() {
		for (String reportID : reports.keySet()) {
			Report report = reports.get(reportID);
			saveReport(report);
		}
	}
	
	private void saveAllCameras() {
		for (String cameraID : cameras.keySet()) {
			SecurityCamera camera = cameras.get(cameraID);
			saveSecurityCamera(camera);
		}
	}
	
	private void saveAllEmployees() {
		for (String employeeID : employees.keySet()) {
			Employee employee = employees.get(employeeID);
			saveEmployee(employee);
		}
	}
	
	private void loadAllGarages() {
		String dir = getFullSubdir(GARAGE_SUBDIR);
		File folder = new File(dir);
		for (File garageFile : folder.listFiles()) {
			loadGarage(garageFile);
		}
	}
	
	private void loadAllTickets() {
		String dir = getFullSubdir(TICKET_SUBDIR);
		File folder = new File(dir);
		for (File ticketFile : folder.listFiles()) {
			loadTicket(ticketFile);
		}
	}
	
	private void loadAllReports() {
		String dir = getFullSubdir(REPORT_SUBDIR);
		File folder = new File(dir);
		for (File reportFile : folder.listFiles()) {
			loadReport(reportFile);
		}
	}
	
	private void loadAllCameras() {
		String dir = getFullSubdir(CAMERA_SUBDIR);
		File folder = new File(dir);
		for (File cameraFile : folder.listFiles()) {
			loadCamera(cameraFile);
		}
	}
	
	private void loadAllEmployees() {
		String dir = getFullSubdir(EMPLOYEE_SUBDIR);
		File folder = new File(dir);
		for (File employeeFile : folder.listFiles()) {
			loadEmployee(employeeFile);
		}
	}
	
	private void loadGarage(File garageFile) {
	    try (Scanner lineScanner = new Scanner(garageFile)) {
	        // read file
	        String garageData = lineScanner.nextLine();
	        if (!isValidGarageData(garageData)) {
	            log.append(LogType.ERROR, "Invalid garage data from "+garageFile.getName()+". Skipping...");
	            return;
	        }
	        String split[] = garageData.split(",");
	        // parameters
	        String name = split[0];
	        double hourlyRate = Double.parseDouble(split[1]);
	        int capacity = Integer.parseInt(split[2]);
	        double gateOpenTime = Double.parseDouble(split[3]);
	        // assemble object
	        Garage garage = new Garage(name, hourlyRate, capacity, gateOpenTime);
	        // add garage to database
	        garages.put(garage.getID(), garage);
	    } catch (Exception e) {
	        log.append(LogType.ERROR, e+" while loading garage from "+garageFile.getName()+". Skipping...");
	    }
	}

	private void loadTicket(File ticketFile) {
	    try (Scanner lineScanner = new Scanner(ticketFile)) {
	        // load from file
	        String ticketData = lineScanner.nextLine();
	        if (!isValidTicketData(ticketData)) {
	            log.append(LogType.ERROR, "Invalid ticket data from "+ticketFile.getName()+". Skipping...");
	            return;
	        }
	        String split[] = ticketData.split(",");
	        // parameters
	        String garageID = split[0];
	        String entryTimeStr = split[1];
	        String exitTimeStr = split[2];
	        Boolean overridden = Boolean.parseBoolean(split[3]);
	        Boolean paid = Boolean.parseBoolean(split[4]);
	        String feeStr = split[5];
	        // null string check
	        Long entryTime = null;
	        if (!entryTimeStr.equals("null")) {
	            entryTime = Long.parseLong(entryTimeStr);
	        }
	        Long exitTime = null;
	        if (!exitTimeStr.equals("null")) {
	            exitTime = Long.parseLong(exitTimeStr);
	        }
	        Double fee = null;
	        if (!feeStr.equals("null")) {
	            fee = Double.parseDouble(feeStr);
	        }
	        // find associated garage from id
	        Garage garage = garages.get(garageID);
	        if (garage == null) {
	            log.append(LogType.ERROR, "Unable to find garage for ticket "+ticketFile.getName()+". Skipping...");
	            return;
	        }
	        // assemble object
	        Ticket ticket = new Ticket(garage, new Date(entryTime), new Date(exitTime), overridden, paid, fee);
	        // add ticket to database
	        tickets.put(ticket.getID(), ticket);
	        // add ticket to associated garage
	        garage.loadTicket(ticket);
	    } catch (Exception e) {
	        log.append(LogType.ERROR, e+" while loading ticket from "+ticketFile.getName()+". Skipping...");
	    }
	}

	private void loadReport(File reportFile) {
	    try (Scanner lineScanner = new Scanner(reportFile)) {
	        // load from file
	        String garageID = lineScanner.nextLine();
	        String entriesStr = lineScanner.nextLine();
	        String earningsStr = lineScanner.nextLine();
	        // find associated garage from id
	        Garage garage = garages.get(garageID);
	        if (garage == null) {
	            log.append(LogType.ERROR, "Unable to find garage for report "+reportFile.getName()+". Skipping...");
	            return;
	        }
	        // get entries and earnings from strings
	        ArrayList<Date> entryTimes = getEntryTimesFromString(entriesStr);
	        ArrayList<Earning> earnings = getEarningsFromString(earningsStr);
	        // assemble object
	        Report report = new Report(garage);
	        for (Date entryTime : entryTimes) {
	            report.addEntryTime(entryTime);
	        }
	        for (Earning earning : earnings) {
	            report.addExit(earning);
	        }
	        // add report to database
	        reports.put(report.getID(), report);
	        // add report to garage
	        garage.loadReport(report);
	    } catch (Exception e) {
	        log.append(LogType.ERROR, e+" while loading report from "+reportFile.getName()+". Skipping...");
	    }
	}

	private void loadCamera(File cameraFile) {
	    try (Scanner lineScanner = new Scanner(cameraFile)) {
	        // load from file
	        String cameraData = lineScanner.nextLine();
	        if (!isValidSecurityCameraData(cameraData)) {
	            log.append(LogType.ERROR, "Invalid camera data from "+cameraFile.getName()+". Skipping...");
	            return;
	        }
	        // find associated garage from id
	        Garage garage = garages.get(cameraData);
	        if (garage == null) {
	            log.append(LogType.ERROR, "Unable to find garage for camera "+cameraFile.getName()+". Skipping...");
	            return;
	        }
	        // assemble object
	        SecurityCamera camera = new SecurityCamera(garage);
	        // add camera to database
	        cameras.put(camera.getID(), camera);
	        // add camera to garage
	        garage.addCamera(camera);
	    } catch (Exception e) {
	        log.append(LogType.ERROR, e+" while loading camera from "+cameraFile.getName()+". Skipping...");
	    }
	}

	private void loadEmployee(File employeeFile) {
	    try (Scanner lineScanner = new Scanner(employeeFile)) {
	        // load from file
	        String employeeData = lineScanner.nextLine();
	        if (!isValidEmployeeData(employeeData)) {
	            log.append(LogType.ERROR, "Invalid employee data from "+employeeFile.getName()+". Skipping...");
	            return;
	        }
	        String split[] = employeeData.split(",");
	        // parameters
	        String garageID = split[0];
	        String username = split[1];
	        String password = split[2];
	        // find associated garage from id
	        Garage garage = garages.get(garageID);
	        if (garage == null) {
	            log.append(LogType.ERROR, "Unable to find garage for employee "+employeeFile.getName()+". Skipping...");
	            return;
	        }
	        // assemble object
	        Employee employee = new Employee(garage, username, password);
	        // add employee to databases
	        addEmployeeToDatabases(employee);
	    } catch (Exception e) {
	        log.append(LogType.ERROR, e+" while loading employee from "+employeeFile.getName()+". Skipping...");
	    }
	}
	
	private ArrayList<Date> getEntryTimesFromString(String reportEntriesStr) { // returns the instantiated entry times from a report file's line of entries 
		ArrayList<Date> entryTimes = new ArrayList<>();
		String split[] = reportEntriesStr.split(",");
		for (String s : split) {
			try {
				Long entryTime = Long.parseLong(s);
				entryTimes.add(new Date(entryTime));
			} catch (Exception e) {
				continue;
			}
		}
		return entryTimes;
	}
	
	private ArrayList<Earning> getEarningsFromString(String reportEarningsStr) { // returns the instantiated earnings from a report file's line of earnings 
		ArrayList<Earning> earnings = new ArrayList<>();
		String splitData[] = reportEarningsStr.split("\\|");
		for (String earning : splitData) {
			try {
				String splitEarning[] = earning.split(",");
				Long exitTime = Long.parseLong(splitEarning[0]);
				double revenue = Double.parseDouble(splitEarning[1]);
				if (revenue < 0) { // no negative revenue!
					continue;
				}
				earnings.add(new Earning(new Date(exitTime), revenue));
			} catch (Exception e) {
				continue;
			}
		}
		return earnings;
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
		// parameters
		//String garageID = split[0];
		String entryTimeStr = split[1];
		String exitTimeStr = split[2];
		String overriddenStr = split[3];
		String paidStr = split[4];
		String feeStr = split[5];
		// typecast conversion check
		@SuppressWarnings("unused")
		Long entryTime;
		@SuppressWarnings("unused")
		Long exitTime;
		@SuppressWarnings("unused")
		boolean overridden;
		@SuppressWarnings("unused")
		boolean paid;
		double fee = 0;
		try { // only tickets are allowed to have null values
			if (!entryTimeStr.equals("null")) {
				entryTime = Long.parseLong(entryTimeStr);
			}
			if (!exitTimeStr.equals("null")) {
				exitTime = Long.parseLong(exitTimeStr);
			}
			overridden = Boolean.parseBoolean(overriddenStr);
			paid = Boolean.parseBoolean(paidStr);
			if (!feeStr.equals("null")) {
				fee = Double.parseDouble(feeStr);
			}
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
	
	private String getFullSubdir(String subdir) { // returns full subdirectory path
		return Paths.get(rootDir, subdir).toString();
	}
}