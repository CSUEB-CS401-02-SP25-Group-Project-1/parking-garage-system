package ParkingLot;

public class User {
	protected Garage garage;
	protected UserType userType; 
	
	public User() {
		this.garage = null;
		this.userType = UserType.Undefined;
	}
	
	public User(Garage garage) {
		this.garage = garage;
		this.userType = UserType.Undefined;
		Ticket ticket = new Ticket();
		Receipt receipt = new Receipt(ticket.getID());
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
	
	public void printReceipt(Ticket ticketID){
		receipt.receipt(ticketID);
		System.out.println("Ticket total: \n" + receipt.toString());
		
	}
}
