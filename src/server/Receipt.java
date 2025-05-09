package server;

import java.util.Date;

import interfaces.ReceiptInterface;

public class Receipt implements ReceiptInterface 
{
	private String ticketID;
	private String garageName;
	private Date entryTime;
	private Date exitTime;
	private double paymentAmount;
	
	public Receipt(String ticketID, String garageName, Date entryTime, Date exitTime, double paymentAmount) {
		this.ticketID = ticketID;
		this.garageName = garageName;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.paymentAmount = paymentAmount;
	}
	
	public Receipt(Ticket ticket) {
		this.ticketID = ticket.getID();
		this.garageName = ticket.getGarage().getName();
		this.entryTime = ticket.getEntryTime();
		this.exitTime = ticket.getExitTime();
		this.paymentAmount = ticket.getFee();
	}
	
	public String toString() {
		return "RECEIPT FOR " + ticketID + "\n" + 
		"Garage: " + garageName + "\n" +
		"Ticket ID: " + ticketID + "\n" +
		"Entry Time: " + entryTime + "\n" +
		"Exit Time: " + exitTime + "\n" +
		"Payment Amount: $" + String.format("%2f",paymentAmount); 
	}
}