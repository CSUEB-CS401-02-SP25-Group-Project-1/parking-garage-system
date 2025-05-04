package server;

import interfaces.UserInterface;

public class User implements UserInterface {
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
	
	public void setGarage(Garage garage){
		this.garage = garage;
	}
	
	public Garage getGarage() {
		return this.garage;
	}
	
	public UserType getType() {
		return this.userType;
	}

	public String generateTicket() {	
		return garage.generateTicket();
	}
	
	public Receipt payTicket(String ticketID, double paymentAmount) {	
		return garage.payTicket(ticketID, paymentAmount);
	}

	public int getAvailableSpaces() {
		return garage.getAvailableSpaces();
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
		//If ticket was NOT found, return null
		return null;
	}

	public String getGarageName() {
		return garage.getName();
	}

	public void toggleGate() {
		Gate gate = garage.getGate();
		if(gate.isOpen())
			gate.close();
		else
			gate.open();
	}
	
	public boolean getGateStatus() {
		return garage.getGate().isOpen();
	}
	
	public double getGateOpenTime() {
		return garage.getGate().getOpenTime();
	}

	
}
