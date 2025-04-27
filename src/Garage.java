import java.util.*;

public class Garage {
	
	// Parameters
	private static int nextId = 0;
	private final String id; // Individual Garage's ID
	private String name; // Individual Garage's name
	private int capacity; // Total of an individual garage
	private double hourlyRate; // Garage's hourly rate used for calculating fees
	
	private final List<Ticket> allTickets = new ArrayList<>(); // List of all tickets ever created
	
	private final List<Ticket> activeTickets = new ArrayList<>(); // List of all ACTIVE tickets
	
	private final Report report; // Revenue report for the garage
	private final Gate gate = new Gate(); // Gate associated with the parking garage
	private final List<SecurityCamera> cams = new ArrayList<>(); // Associated Security Cameras
	
	// CONSTRUCTORS
	
	// No argument constructor for testing
	public Garage() {
		this("Unnamed", 1, 0.0);
	}
	//Main constructor
	public Garage(String name, int capacity, double hourlyRate) {
		//Input validation
		if(name.equals(""))
			throw new IllegalArgumentException("Garage name cannot be empty");
		if(capacity <= 0)
			throw new IllegalArgumentException("Garage capacity must be greater than zero");
		if(hourlyRate < 0)
			throw new IllegalArgumentException("Hourly rate must be greater than or equal to zero");
		
		this.id = "GA" + nextId++;
		this.name = name;
		this.capacity = capacity;
		this.hourlyRate = hourlyRate;
		this.report = new Report();
	}
	
	// GETTERS & SETTERS
	public String getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		if(name.equals(""))
			throw new IllegalArgumentException("Garage name cannot be empty");
		this.name = name;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	public void setCapacity(int capacity) {
		if(capacity <= 0)
			throw new IllegalArgumentException("Garage capacity must be greater than zero");
		this.capacity = capacity;
	}
	
	public double getHourlyRate() {
		return this.hourlyRate;
	}
	public void setHourlyRate(double hourlyRate) {
		if(hourlyRate < 0)
			throw new IllegalArgumentException("Hourly rate must be greater than or equal to zero");
		this.hourlyRate = hourlyRate;
	}
	
	public int getAvailableSpaces() {
		// Availability = capacity - active spots
		return this.capacity - activeTickets.size();
	}
	public Boolean isFull() {
		return capacity == activeTickets.size();
	}
	
	// TICKETING
	
	//Upon entry: 
	//1. Add entry to report
	//2. Generate a new ticket
	//3. Add new ticket to both lists of tickets
	//4. Return the newly created ticket
	public Ticket generateTicket() {
		if(this.isFull())
			throw new RuntimeException("Garage is full, cannot generate ticket");
		
		//Update report
		this.report.addEntry();
		
		//Generate new ticket
		Ticket ticket = new Ticket(this);
		
		//Add new ticket to both lists of tickets
		this.activeTickets.add(ticket);
		this.allTickets.add(ticket);
		
		//Return the new ticket
		return ticket;
	}
	//Upon ticket payment:
	//1. Ticket removed from activeTickets
	//2. Ticket marked as paid in allTickets
	//3. Report is updated
	//4. Receipt is generated and returned
	public Receipt payTicket(String ticketId, double amount) {
		Ticket ticket;
		
		// Remove ticket from activeTickets
		for(int i = 0; i < this.activeTickets.size(); i++){
			if(this.activeTickets.get(i).getID() == ticketId){
				ticket = this.activeTickets.get(i);
				this.activeTickets.remove(i);
				break;
			}
		}
		
		//Find ticket in allTickets and mark as paid
		for(int i = 0; i < this.allTickets.size(); i++){
			if(this.allTickets.get(i).getID() == ticketId){
				this.activeTickets.get(i).pay((float)amount);
				break;
			}
		}
		
		//Update Report
		this.report.addExit((float)amount);
		
		// Generate and return receipt
		return new Receipt(ticket);
	}
	
	// REPORTING
	public String viewReport() {
		return this.report.toString();
	}
	public double getTotalRevenue() {
		// TO BE IMPLEMENTED
	}
	
	// SECURITY CAMERAS
	public boolean addCamera(SecurityCamera cam) {
		this.cams.add(cam);
	}
	//Loop through the list of cameras and remove the matching one if found
	public boolean removeCamera(String cameraId) {
		for(int i = 0; i < this.cams.size(); i++) {
			if(this.cams.get(i).getId == cameraId) {
				this.cams.remove(i);
				return true;
			}
		}
		return false;
	}
	public List<SecurityCamera> getCameras() {
		return this.cams;
	}
	
	// Misc
}
