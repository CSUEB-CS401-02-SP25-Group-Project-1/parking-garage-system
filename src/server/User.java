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
	
	public Receipt payTicket(String ticketID) {	
		return garage.payTicket(ticketID);
	}

	public int getAvailableSpaces() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String viewBillingSummary(String ticketID) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getGarageName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void toggleGate() {
		// TODO Auto-generated method stub
		
	}
}
