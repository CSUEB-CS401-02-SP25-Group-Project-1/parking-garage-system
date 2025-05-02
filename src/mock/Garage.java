package mock;

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
	
	public void loadTicket(Ticket ticket) { // used by server to load existing tickets to both arrays
		if (!ticket.isPaid()) {
			activeTickets.add(ticket);
		}
		allTickets.add(ticket);
	}
	
	public Ticket getTicket(String ticketID) { // returns ticket from all tickets list based on its ticket id
		for (Ticket ticket : allTickets) {
			if (ticket.getID().equals(ticketID)) {
				return ticket;
			}
		}
		return null;
	}

	public Receipt payTicket(String ticketID) { // returns receipt if successful
		// do logic here for finding ticket based on ticket ID
		// attempt to process payment
		// if payment went through, create a new receipt
		// if ticket was already paid for, return null instead
	
		for (int i = 0; i < activeTickets.size(); i++) {
			Ticket curTicket = activeTickets.get(i);
			if (curTicket.getID().equals(ticketID)) {
				curTicket.pay(); // mark as paid
				activeTickets.remove(curTicket); // remove from active tickets list
			}
		}

		return new Receipt("TI602", "The Awesome Garage", new Date(), new Date(), 599.99); // dummy value
	}
	
	public void loadReport(Report report) { // used by server to load report from file
		this.report = report;
	}

	public Report viewReport() {
		return null; // dummy value
	}

	public boolean addCamera(SecurityCamera newCamera) {
		// add camera to garage's camera list if camera hasn't been added yet
		// return true if added successfully
		// return false if camera couldn't be added (duplicate cameras)
		
		cameras.add(newCamera);
		return true; // dummy value
	}

	public boolean removeCamera(String cameraID) {
		// remove camera based on its camera ID
		// return true if camera was successfully removed
		// return false if no camera with such ID was found
		
		return true; // dummy value
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
