package mock;

import interfaces.CustomerInterface;
import server.Garage;
import server.UserType;

public class Customer extends User implements CustomerInterface {
	
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
