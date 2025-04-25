
public class Garage {
	
	// Parameters
	private static int nextId = 0;
	private final String id; // Individual Garage's ID
	private String name; // Individual Garage's name
	private int capacity; // Total of an individual garage
	private double hourlyRate; // Garage's hourly rate used for calculating fees
	
	private final Ticket[] allTickets; // List of all ACTIVE tickets
	
	private final Ticket[] activeTickets; // List of all tickets ever created
	
	private final Report report; // Revenue report for the garage
	private final Gate gate; // Gate associated with the parking garage
	private final SecurityCamera[] cams; // Associated Security Cameras
	
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
