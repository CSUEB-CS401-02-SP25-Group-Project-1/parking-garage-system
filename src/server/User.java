package server;

import interfaces.UserInterface;

public class User implements UserInterface{
	protected Garage garage;
	protected UserType userType; 
	
	public User() {
		this.garage = null;
		this.userType = UserType.Undefined;
	}
	
	public User(Garage garage) {
		this.garage = garage;
		this.userType = UserType.Undefined;
	}
	
	public void setGarage(Garage garage){this.garage = garage;}
	public Garage getGarage() {return this.garage;}
	public UserType getType() {return this.userType;}

	
	public Receipt payTicket(String ticketID) {		//ticket
		Ticket ticket = garage.getTicket(ticketID);
		ticket.pay(ticket.getFee());
		Receipt receipt = new Receipt(ticket, ticket.getFee());
		System.out.println(" Parking Fee Total: " + ticket.toString() + "\n Ticket Successfully Paid");
		return receipt;
	}
	
	public void printReceipt(Ticket ticket){
		Ticket tempTicket = garage.getTicket(ticket.getID());
		if(tempTicket != null && tempTicket.isPaid() == true) {
			Receipt receipt = new Receipt(tempTicket, ticket.getFee());
			System.out.println("Receipt Total: \n" + receipt.toString());
		}
	}
	
	public String generateTicket() {		//garage and ticket
		String ticketID = garage.generateTicket();
		if(ticketID != null) {
			return "Ticket has been generated.\n Ticket ID: " + ticketID;
		}
		return null;
	}
	
	public int getAvailableSpaces() {
		int currentAvailableSpots = this.garage.getCapacity();
		return currentAvailableSpots;
	}
	
	public Receipt payTicket1(String ticketID) {  
		if(this.garage != null) {
			Ticket ticket = this.garage.getTicket(ticketID);
			Receipt receipt = new Receipt(ticketID, getGarageName(), ticket.getEntryTime(), ticket.getExitTime(), (garage.getHourlyRate() * 4));
			receipt.toString();
			return receipt;
		}
		return null;
	}
	
	public String getGateStatus() {
		if(this.garage != null) {
			Gate gate = this.garage.getGate();
			if(gate.open)
				return "Gate is opened";
			else
				return "Gate is closed";
		}
		return null;
	}
	
	public void toggleGate() {
		if(this.garage != null) {
			Gate gate = this.garage.getGate();
			Log serverLog = new Log();
			new Thread(new GateHandler(gate, serverLog)).start();
		}
	}
	
	public String viewBillingSummary(String ticketID) {
		//Find ticket
		Ticket ticket = garage.getTicket(ticketID);
		
		//If ticket was found, return billing summary
		if(ticket != null)
			return ticket.getID() + ":" +
					ticket.getEntryTime() + ":" +
					ticket.getExitTime() + ":" +
					ticket.getFee();
		else
			System.out.println("Ticket not found!");
		//If ticket was NOT found, return null
		return null;
	}
	
	public String getGarageName() {
		String garageName = null;
		if(this.garage != null) {
			garageName = this.garage.getName();
		}
		return garageName;
	}

	@Override
	public Receipt payTicket(String ticketID, double paymentAmount){ 
		if(this.garage != null) {
			Ticket ticket = this.garage.getTicket(ticketID);
			Receipt receipt = new Receipt(ticketID, getGarageName(), 
					ticket.getEntryTime(), ticket.getExitTime(), ticket.getFee());
			receipt.toString();
			
			return receipt;
		}
		return null;
	}
}
