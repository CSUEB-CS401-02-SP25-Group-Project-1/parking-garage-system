package interfaces;

import java.util.ArrayList;

import mock.Gate;
import server.Receipt;
import server.Report;
import server.SecurityCamera;

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
	Receipt payTicket(String ticketID);
	Report viewReport();
	boolean addCamera(SecurityCamera newCamera);
	boolean removeCamera(String cameraID);
	ArrayList<SecurityCamera> getCameras();
	void addLogEntry(String logEntry);
	ArrayList<String> getLogEntries();
	Gate getGate();
}
