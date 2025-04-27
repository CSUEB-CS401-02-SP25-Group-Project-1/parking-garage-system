package mock;

import interfaces.CustomerInterface;

public class Customer extends User implements CustomerInterface {
	
	public Customer(Garage garage) {
		this.garage = garage;
	}

	public int viewGarageAvailability() {
		return garage.getAvailableSpaces();
	}

}
