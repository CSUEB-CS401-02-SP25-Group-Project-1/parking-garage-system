package mock;

import java.util.ArrayList;

public class Garage {
	private static int count = 0;
	private final String id; // unique garage id
	private String name; // human-readable garage name (e.g., location)
	private int capacity; // garage's maximum parking capacity
	private double hourlyRate; // garage's hourly rate used for calculating fees
	private ArrayList<Ticket> allTickets; // all tickets ever aggregated with the garage
	private ArrayList<Ticket> activeTickets; // currently parked vehicles in the garage (represented by active tickets)
	private Report report; // revenue report for the garage
	private Gate gate; // gate associated with the parking garage
	private ArrayList<SecurityCamera> cams; // aggregated security cameras
	private ArrayList<String> logEntries; // log entries for a particular garage
	
	// Constructors
	public Garage() {
		id = "GA"+count++;
	}
	
	public Garage(int capacity, double hourlyRate, String name) {
		id = "GA"+count++;
	}
	
	// Getters & Setters
	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public double getHourlyRate() { // returns fixed rate of garage (not to be confused with hourly earnings)
		return hourlyRate;
	}
	
	public void setHourlyRate(double newRate) { // sets new fixed rate for garage (not to be confused with hourly earnings)
		this.hourlyRate = newRate;
	}
	
	public int getAvailableSpaces() {
		return (capacity - activeTickets.size());
	}
	
	public Boolean isFull() {
		return (capacity == activeTickets.size());
	}
	
	// Ticketing
	public String generateTicket() { // adds new ticket to both ticket lists

	}
	
	public Receipt payTicket(String ticketID, double amount) { // removes ticket from activeTicket list
		
	}
	
	// Reporting
	public Report viewReport(UserType requester) {
		// TODO: make dummy report
	}
	
	public double getTotalRevenue() {
		return 420.07; // dummy value
	}
	
	// Security Cameras
	public boolean addCamera(SecurityCamera cam) {
		cams.add(cam);
	}
	
	public boolean removeCamera(String cameraID) {
		
	}
	
	public ArrayList<SecurityCameras> getCameras()
	{
		return cams;
	}
	
	// Misc
	public void addLogEntry(String logString) { // adds log entry (as a string) to garage
		logEntries.add(logString);
	}
	
	public ArrayList<String> getLogEntries() {
		return logEntries;
	}
}

