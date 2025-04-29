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
	
	public void loadAll() {
		loadGarages();
		loadTickets(); // tickets must be loaded after garages
		loadReports(); // reports must be loaded after garages
		loadCameras(); // cameras must be loaded after garages
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
            int gateOpenTime = Integer.parseInt(split[3]);
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
			String curData = lineScanner.nextLine();
			lineScanner.close(); // close current line scanner instance
			if (!isValidGarageData(curData)) {
				continue; // skip if data is invalid
			}
			// parsing into object
			String split[] = curData.split(",");
			String garageID = split[0];
			Date entryTime = getDateFromString(split[1]);
			Date exitTime = getDateFromString(split[2]);
			boolean overridden = Boolean.parseBoolean(split[3]);
			boolean paid = Boolean.parseBoolean(split[4]);
			double fee = Double.parseDouble(split[5]);
			Garage garage = getGarage(garageID);;
			// check if associated garage exists in database
			if (garage == null) {
				continue; // skip loading ticket if it doesn't
			}
			// add ticket to database
			Ticket curTicket = new Ticket(garage, exitTime, exitTime, paid, paid, fee);
			tickets.put(curTicket.getID(), curTicket);
		}
	}
	
	private void loadReports() {
		
	}
	
	private void loadCameras() {
		
	}
	
	private void loadEmployees() {
		
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
        int gateOpenTime;
		try { // typecast conversion check
			//name = split[0];
            hourlyRate = Double.parseDouble(split[1]);
            capacity = Integer.parseInt(split[2]);
            gateOpenTime = Integer.parseInt(split[3]);
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
		Date entryTime;
		Date exitTime;
		boolean overridden;
		boolean paid;
		double fee;
		try { // typecast conversion check
			//garageID = split[0];
			entryTime = getDateFromString(split[1]);
			exitTime = getDateFromString(split[2]);
			overridden = Boolean.parseBoolean(split[3]);
			paid = Boolean.parseBoolean(split[4]);
			fee = Double.parseDouble(split[5]);
		} catch (Exception e) {
			return false;
		}
		if (entryTime == null) { // null check entry time
			return false;
		}
		if (exitTime == null) { // null check exit time
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
	
	private Date getDateFromString(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
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
