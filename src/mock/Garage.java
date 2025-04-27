package mock;

import java.util.ArrayList;
import interfaces.GarageInterface;
import mock.Receipt;
import mock.Report;
import mock.SecurityCamera;

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
	
	public Garage(String name, double hourlyRate, int capacity, int gateOpenTime) {
		id = "GA"+count++;
		this.name = name;
		this.hourlyRate = hourlyRate;
		this.capacity = capacity;
		gate = new Gate(gateOpenTime);
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
			Ticket ticket = new Ticket();
			allTickets.add(ticket);
			activeTickets.add(ticket);
			return ticket.getID();
		}
		return null;
	}

	public Receipt payTicket(String ticketID) { // returns receipt if successful
		// do logic here for finding ticket based on ticket ID
		// attempt to process payment
		// if payment went through, create a new receipt
		// if ticket was already paid for, return null instead
		
		return new Receipt(); // dummy value
	}

	public Report viewReport() {
		return new Report(); // dummy value
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
}
