package mock;

import java.util.Date;
import interfaces.UserInterface;
import server.Garage;
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
		return garage.generateTicket();
	}
	
	public Receipt payTicket(String ticketID) {	
		return garage.payTicket(ticketID);
	}
}
