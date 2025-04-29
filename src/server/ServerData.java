package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
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
		loadAllGarages();
		loadAllTickets();
		loadAllReports();
		loadAllCameras();
		loadAllEmployees();
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
	private void loadAllGarages() {
		
	}
	
	private void loadAllTickets() {
		
	}
	
	private void loadAllReports() {
		
	}
	
	private void loadAllCameras() {
		
	}
	
	private void loadAllEmployees() {
		
	}
	
	private void loadGarage(File garageFile) {
		try {
			Scanner lineScanner = new Scanner(garageFile);
			String garageData = lineScanner.nextLine();
			lineScanner.close();
			if (!isValidGarageData(garageData)) {
				log.append(LogType.ERROR, "Invalid garage data from "+garageFile.getName()+". Skipping...");
				return;
			}
			String split[] = garageData.split(",");
			String name = split[0];
			double hourlyRate = Double.parseDouble(split[1]);
	        int capacity = Integer.parseInt(split[2]);
	        double gateOpenTime = Double.parseDouble(split[3]);
	        Garage garage = new Garage(name, hourlyRate, capacity, gateOpenTime);
	        garages.put(garage.getID(), garage);
		} catch (Exception e) {
			log.append(LogType.ERROR, e+" while loading garage from"+garageFile.getName());
		}
	}
	
	private void loadTicket(File ticketFile) {
		try {
			Scanner lineScanner = new Scanner(ticketFile);
			String ticketData = lineScanner.nextLine();
			lineScanner.close();
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
				log.append(LogType.ERROR, "Unable to find garage for "+ticketFile.getName()+". Skipping...");
				return;
			}
			// assemble into object
			Ticket ticket = new Ticket(garage, new Date(entryTime), new Date(exitTime), overridden, paid, fee);
			// add ticket to database
			tickets.put(ticket.getID(), ticket);
			// add ticket to associated garage
			garage.loadTicket(ticket);
		} catch (Exception e) {
			log.append(LogType.ERROR, e+" while loading ticket from"+ticketFile.getName());
		}
	}
	
	private void loadReport(File reportFile) {
		
	}
	
	private void loadCamera(File cameraFile) {
		
	}
	
	private void loadEmployee(File employeeFile) {
		
	}
	
	private ArrayList<Date> getEntryTimes(String reportEntriesStr) { // returns the instantiated entry times from a report file's line of entries 
		ArrayList<Date> entryTimes = new ArrayList<>();
		// TODO: business logic
		return entryTimes;
	}
	
	private ArrayList<Earning> getEarnings(String reportEarningsStr) { // returns the instantiated earnings from a report file's line of earnings 
		ArrayList<Earning> earnings = new ArrayList<>();
		// TODO: business logic
		return earnings;
	}
	
	private boolean isValidEarningData(String earningData) {
		// TODO: business logic
		return true;
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
		try { 
			if (entryTimeStr != null) {
				entryTime = Long.parseLong(entryTimeStr);
			}
			if (exitTimeStr != null) {
				exitTime = Long.parseLong(exitTimeStr);
			}
			overridden = Boolean.parseBoolean(overriddenStr);
			paid = Boolean.parseBoolean(paidStr);
			if (feeStr != null) {
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
