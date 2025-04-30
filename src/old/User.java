package server;

public class User implements UserInterface{

	private Garage garage;
	private UserType userType;

	// constructors:
	public User() {
		garage = new Garage();
		userType = UserType.Undefined;
	}

	public User(Garage garage) {
		this.garage = garage;
		userType = UserType.Undefined;
	}

	public User(Garage garage, UserType userType) {
		this.garage = garage;
		this.userType = userType;
	}

	// methods from interface:
	public void setGarage(Garage newGarage) {
		garage = newGarage;
	}

	public void setUserType(UserType newType) {userType = newType;}

	public Garage getGarage() {return garage;}
	public UserType getType() {return userType;}

	// return type of `Ticket`? to match Employee?
	public Ticket generateTicket() {
		return garage.generateTicket();
	}

	// payTicket needs amount passed into it
	public Receipt payTicket(String ticketID, double amount) {
		// garage.payTicket() already returns a receipt
		return garage.payTicket(ticketID, amount);
	}
}
