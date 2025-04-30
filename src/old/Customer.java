package server;

public class Customer extends User implements CustomerInterface{

	public Customer(Garage garage) {
		this.garage = garage;
		userType = UserType.Customer;
	}

	public Customer() { // no arguments constructor for testing
		garage = new Garage();
		userType = UserType.Customer;
	}

	// most methods a Customer will use come from the parent class User

	public int viewGarageAvailability() {
		return garage.getAvailableSpace();
	}
}
