package interfaces;

import java.util.ArrayList;
import server.Gate;
import server.Receipt;
import server.Report;
import server.SecurityCamera;
import server.Ticket;

public interface GarageInterface {
	String getID();
	String getName();
	void setName(String newName);
	int getCapacity();
	double getHourlyRate();
	void setHourlyRate(double newRate);
	int getAvailableSpaces();
	boolean isFull();
	String generateTicket(); // returns ticket ID of generated ticket
	void loadTicket(Ticket ticket);
	Ticket getTicket(String ticketID);
	Receipt payTicket(String ticketID, double paymentAmount);
	void loadReport(Report report);
	Report viewReport();
	boolean addCamera(SecurityCamera newCamera);
	boolean removeCamera(String cameraID);
	ArrayList<Ticket> getActiveTickets();
	ArrayList<Ticket> getAllTickets();
	ArrayList<SecurityCamera> getCameras();
	SecurityCamera getCamera(String cameraID);
	void addLogEntry(String logEntry);
	ArrayList<String> getLogEntries();
	Gate getGate();
}
