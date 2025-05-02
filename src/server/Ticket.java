package server;

import interfaces.TicketInterface;

import java.util.Date;

// new ticket class to support server and TicketInterface


public class Ticket implements TicketInterface{
	
	// attributes
	private static int count = 0;
	private String id;

	private Date entryTime;
	private Date exitTime;

	private Garage garage; // garage handle lets tickets be semi-autonomous.
	
	private boolean isPaid;
	private boolean isOverridden;

	private double fee;
	
	// needs:
		// override()		// overrides ticket with 0 fee
		// override(double fee) // overrides ticket with non-0 fee
		// pay(float amount)	// pays ticket. needs amount passed in
		// isOverridden		// boolean value
		// isPaid		// boolean value
		// toString()
		 	// "<GID>,<entry>,<exit>,<overridden>,<paid>,<fee>"
		// getters
		// setters
		// constructor
	
	//constructors
	public Ticket() {
		// no arguments constructor
		id = "T" + (count++);
		entryTime = new Date();
		exitTime = null;
		isPaid = false;
		isOverridden = false;
		fee = 0.0;

		this.garage = new Garage(); // fake garage for testing

	}

	public Ticket(Garage garage) {
		// one argument constructor (standard)
		// entryTime is now
		// exitTime is null
		// isPaid is false
		// isOverridden is false
		// garage reference is passed
		id = "T" + (count++);

		entryTime = new Date();

		exitTime = null;
		isPaid = false;
		isOverridden = false;
		fee = 0.0;

		this.garage = garage;
	}

	public Ticket(Garage garage, Date entryTime, Date exitTime,
			boolean isOverridden, boolean isPaid, double fee) {
		// many-arguments constructor for ServerData
		id = "T" + (count++);
		this.garage = garage;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.isOverridden = isOverridden;
		this.isPaid = isPaid;
		this.fee = fee;
	}

	//getters
	public Garage getGarage() {return garage;}
	public Date getEntryTime() {return entryTime;}
	public Date getExitTime() {return exitTime;}
	public boolean isOverridden() {return isOverridden;}
	public boolean isPaid() {return isPaid;}
	public String getID() {return id;}
	public double getFee() {return fee;}

	// setters
	//-----------//

	// methods
	public void overrideFee(double newFee) {
		// override this ticket with a new fee
		// revenue is still counted 
		if (isPaid || isOverridden) {return;}
		// override forces this ticket to be paid
		exitTime = new Date();
		fee = newFee;

		isOverridden = true;
		isPaid = true;
	}
	public void overrideFee() {
		overrideFee(0);
	}
	public void override() {
		overrideFee(0);
	}

	public void calculateFee() {
		// calculates the fee that customer *would* pay
		// stores it in fee
		// does *not* pay the ticket off. this is merely for visibility
		Date now = new Date(); 
		long hours_in_garage =
			(now.getTime() - entryTime.getTime()) / 3600000;
		fee = garage.getHourlyRate() * hours_in_garage;
	}

	public boolean pay(double paymentAmount) {
		// fee has been calculated and returned with getFee()
		if (isPaid || isOverridden) {return false;}

		if (paymentAmount < fee) {return false;} // payment validation
		exitTime = new Date();
		isPaid = true;

		// not overridden
		// don't change fee
		return true;
	}

	public String toString() {
		//format: "<GID>,<entry>,<exit>,<overridden>,<paid>,<fee>"
		String r = garage.getID();
		r += "," + entryTime.getTime(); 
			// .getTime() returns long
			// does not interfere with comma separation
			// and can be restored with new Date(long)
		if (exitTime != null) {
			r += ",null";
		} else {
			r += "," + exitTime.getTime();
		}

		r += "," + isOverridden;
		r += "," + isPaid;
		r += "," + fee;

		return r;
	}
}
