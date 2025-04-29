package mock;

import java.util.Date;
import interfaces.UserInterface;
import server.UserType;

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
		Ticket ticket = garage.generateTicket();
		if(ticket != null) 
			return "Ticket has been generated.\n Ticket ID: " + ticket.getID();
	}
	
	public Receipt payTicket(String ticketID, float amount) {	
		garage.payTicket();
		return garage.getReceipt(ticketID);;
	}
}
