package mock;

import java.util.Date;
import interfaces.TicketInterface;
import server.Garage;

public class Ticket implements TicketInterface {
	private static int count = 0;
	private String id;
	private Garage garage; // associated garage
	private Date entryTime;
	private Date exitTime;
	private boolean overridden = false;
	private boolean paid = false;
	private Double fee;
	
	// constructor for generating new ticket
	public Ticket(Garage garage) {
		id = "TI"+count++;
		this.garage = garage;
		entryTime = new Date();
	}
	
	// constructor for loading ticket from file
	public Ticket(Garage garage, Date entryTime, Date exitTime, boolean overriden, boolean paid, double fee) {
		id = "TI"+count++;
		this.garage = garage;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.overridden = overriden;
		this.paid = paid;
		this.fee = fee;
	}

	public Garage getGarage() {
		return garage;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public Date getExitTime() {
		return exitTime;
	}

	public boolean isOverridden() {
		return overridden;
	}

	public boolean isPaid() {
		return paid;
	}

	public String getID() {
		return id;
	}

	public void overrideFee(double newFee) {
		if (!isPaid()) {
			fee = newFee;
			overridden = true;
		}
	}

	public void calculateFee() { // calculate fee based on how many hours since entry time multiplied by garage's hourly rate
		fee = 12345.67; // dummy value
	}

	public Double getFee() { // "Double" is not a typo, it's to check if fee is equal to null
		if (fee == null) { // if fee hasn't been set yet
			calculateFee(); // auto-calculate it first before returning it
		}
		return fee;
	}

	public void pay() {
		if (!isPaid()) {
			paid = true;
		}
	}

	public String toString() {
		String entryTimeStr = null;
		String exitTimeStr = null;
		String feeStr = null;
		if (entryTime != null) {
			entryTimeStr = entryTime.getTime()+"";
		}
		if (exitTime != null) {
			exitTimeStr = exitTime.getTime()+"";
		}
		if (fee != null) {
			feeStr = fee+"";
		}
		return garage.getID()+","+entryTimeStr+","+exitTimeStr+","+overridden+","+paid+","+feeStr;
	}
}
