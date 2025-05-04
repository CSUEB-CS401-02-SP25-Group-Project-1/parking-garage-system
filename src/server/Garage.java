package server;

import java.util.ArrayList;
import java.util.Date;
import interfaces.GarageInterface;

public class Garage implements GarageInterface {
	private static int count = 0;
	private String id;
	private String name;
	private double hourlyRate;
	private int capacity;
	private ArrayList<Ticket> allTickets = new ArrayList<>(); // every ticket ever associated with garage
	private ArrayList<Ticket> activeTickets = new ArrayList<>(); // currently parked vehicles
	private ArrayList<SecurityCamera> cameras = new ArrayList<>(); // list of cameras associated with garage
	private ArrayList<String> logEntries = new ArrayList<>(); // every log entry related to garage for employees to view (since boot-up)
	private Gate gate;
	private Report report; // add every new entry and exit to report
	
	public Garage(String name, double hourlyRate, int capacity, double gateOpenTime) {
		id = "GA"+count++;
		this.name = name;
		this.hourlyRate = hourlyRate;
		this.capacity = capacity;
		gate = new Gate(this, gateOpenTime);
	}
	
	public Garage() {
		id = "GA"+count++;
		this.name = "Unnamed";
		this.hourlyRate = 0.0;
		this.capacity = 0;
		gate = new Gate(this, 0.0);
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String newName) {
		name = newName;
	}

	public int getCapacity() {
		return capacity;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(double newRate) {
		hourlyRate = newRate;
	}

	public int getAvailableSpaces() {
		return (capacity - activeTickets.size());
	}

	public boolean isFull() {
		return (capacity == activeTickets.size());
	}

	public String generateTicket() { // returns ticket ID if successful
		if (!isFull()) {
			Ticket ticket = new Ticket(this);
			allTickets.add(ticket);
			activeTickets.add(ticket);
			return ticket.getID();
		}
		return null;
	}
	
	@Override
	public void loadTicket(Ticket ticket) { // used by server to load existing tickets to both arrays
		if (!ticket.isPaid()) {
			activeTickets.add(ticket);
		}
		allTickets.add(ticket);
	}
	
	public Ticket getTicket(String ticketID) { // returns ticket from all tickets list based on its ticket id
		for(int i = 0; i < this.allTickets.size(); i++) {
			if(this.allTickets.get(i).getID().equalsIgnoreCase(ticketID))
				return this.allTickets.get(i);
		}
		return null;
	}

	public Receipt payTicket(String ticketID, double paymentAmount) { // returns receipt if successful
		//Find ticket by ID
		Ticket ticket = getTicket(ticketID);
		
		//Return null if the ticket is already paid
		if(ticket.isPaid()) {
			return null;
		}
		
		//Mark ticket paid and remove from active tickets list
		ticket.pay(paymentAmount);
		this.activeTickets.remove(ticket);
		
		//Create and return new receipt of ticket payment
		return new Receipt(ticket);
	}
	
	@Override
	public void loadReport(Report report) { // used by server to load report from file
		this.report = report;
	}

	public Report viewReport() {
		return this.report;
	}

	@Override
	public boolean addCamera(SecurityCamera newCamera) {
		// Check if camera has been added yet and return false if so
		for(int i = 0; i < this.cameras.size(); i++) {
			if(this.cameras.get(i).getID() == newCamera.getID())
				return false;
		}
		
		// Add camera and return true if not found
		cameras.add(newCamera);
		return true;
	}

	public boolean removeCamera(String cameraID) {
		// remove camera based on its camera ID
		for(int i = 0; i < this.cameras.size(); i++) {
			// return true if camera was successfully removed
			if(this.cameras.get(i).getID().equalsIgnoreCase(cameraID)) {
				this.cameras.remove(i);
				return true;
			}
		}
	
		// return false if no camera with such ID was found
		return false;
	}
	
	public SecurityCamera getCamera(String cameraID) {
		// Find security camera matching the given Camera ID and return it
		for(int i = 0; i < this.cameras.size(); i++) {
			if(this.cameras.get(i).getID() == cameraID)
				return this.cameras.get(i);
		}
		//Return null if not found
		return null;
	}

	public ArrayList<SecurityCamera> getCameras() {
		return cameras;
	}

	public void addLogEntry(String logEntry) {
		logEntries.add(logEntry);
	}

	public ArrayList<String> getLogEntries() {
		return logEntries;
	}
	
	public Gate getGate() {
		return gate;
	}
	
	public String toString() {
		return name+","+hourlyRate+","+capacity+","+gate.getOpenTime();
	}

	public ArrayList<Ticket> getActiveTickets() {
		return activeTickets;
	}

	public ArrayList<Ticket> getAllTickets() {
		return allTickets;
	}
}