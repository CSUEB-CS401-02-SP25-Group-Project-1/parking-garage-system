package server;

public class Customer {

	private Garage garage;

	public Customer(Garage garage) {
		this.garage = garage;
	}

	public Customer() { // no arguments constructor for testing
		garage = null;
	}

	public String generateTicket() {
		// generates ticket in garage
		// returns the ID of ticket created

		Ticket generatedTicket = garage.generateTicket();
		return generatedTicket.getID();
	}

	public Receipt payTicket(String ticketID, float amount) {
		// pays ticket at ID
		// returns true/false depending on success
		
		// there are problems in the garage.payTicket() class
		// no checking if ID is valid
		// no checking if payment is valid
		Receipt receipt = garage.payTicket(ticketID, amount);

		// `receipt` is null if payment fails?
		return receipt;
	}

	public String viewReport() {
		String s = "Available spaces: "
			+ garage.getAvailableSpace();
		return s;
	}
}
