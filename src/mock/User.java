package mock;

import interfaces.UserInterface;
import server.Garage;
import server.Receipt;
import server.UserType;

public class User implements UserInterface {
	protected Garage garage; // associated garage
	protected UserType type;

	public void setGarage(Garage newGarage) {
		garage = newGarage;
	}

	public Garage getGarage() {
		return garage;
	}

	public UserType getType() {
		return type;
	}

	public String generateTicket() {
		// generates a new ticket from garage and returns the id of the generated ticket
		return "TI800"; // dummy value
	}

	public Receipt payTicket(String ticketID) {
		// calls garage.payTicket() and returns the returned receipt 
		return new Receipt(); // dummy value
	}

}
