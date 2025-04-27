package mock;

import java.util.Date;
import interfaces.TicketInterface;

public class Ticket implements TicketInterface {
	private static int count = 0;
	private String id;
	private Garage garage; // associated garage
	private Date entryTime;
	private Date exitTime;
	private boolean overridden;
	private boolean paid;
	private Double fee;
	
	public Ticket(Garage garage) {
		id = "TI"+count++;
		this.garage = garage;
		entryTime = new Date();
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

}
