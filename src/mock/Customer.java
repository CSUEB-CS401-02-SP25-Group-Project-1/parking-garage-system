package mock;

import interfaces.UserInterface;
import server.User;
import server.UserType;

public class Customer extends User implements UserInterface {
	
	public Customer() {
		this.garage = null;
		this.userType = UserType.Customer;
	}
	
	public Customer(Garage garage) {
		this.garage = garage;
		this.userType = UserType.Customer;
	}

	public int viewGarageAvailability() {
		return garage.getAvailableSpaces();
	}

}
