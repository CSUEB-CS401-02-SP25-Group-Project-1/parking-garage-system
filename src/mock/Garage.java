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
		
	}
	
	public Garage(int capacity, double hourlyRate, String name) {
		
	}
	
	// Getters & Setters
	public String getId() {
		
	}
	public String getName() {
		
	}
	public void setName(String name) {
		
	}
	
	public int getCapacity() {
		
	}
	public void setCapacity(int capacity) {
		
	}
	
	public double getHourlyRate() {
		
	}
	public void setHourlyRate(double newRate) {
		
	}
	
	public int getAvailableSpaces() {
		
	}
	public Boolean isFull() {

	}
	
	// Ticketing
	public String generateTicket() {

	}
	public Receipt payTicket(String ticketId, double amount) {
		
	}
	
	// Reporting
	public Report viewReport(Usertype requester) {
		
	}
	public double getTotalRevenue() {
		
	}
	
	// Security Cameras
	public boolean addCamera(SecurityCamera cam) {
		
	}
	public boolean removeCamera(String cameraId) {
		
	}
	public SecurityCamera[] getCameras()
	{
		
	}
	
	// Misc
}

