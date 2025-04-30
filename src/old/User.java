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
	
	public void printReceipt(Ticket ticket){
		Ticket ticket = garage.getTicket(ticket);
		if(ticket != null && ticket.isPaid() == true) {
			Receipt receipt = new Receipt(ticket);
			System.out.println("Receipt Total: \n" + receipt.toString());
		}
	}
}
