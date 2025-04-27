package mock;

import java.util.Date;

public class Ticket {
	private static int count = 0;
	private String id;
	private Garage garage;
	private Date entryTime;
	private Date exitTime;
	private boolean isPaid;
	private boolean overridden;
	private double fee;
	
	public Ticket(Garage garage) {
		id = "TI"+count++;
		this.garage = garage;
		entryTime = new Date();
	}
	
	public Garage getGarage() { // returns associated garage
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
	
	public String getID() {
		return id;
	}
	
	public void overrideFee(double amount) {
		fee = amount;
		overridden = true;
	}
	
	public double getFee() {
		return fee;
	}
	
	public void calculateFee() { // calculates fee based on the garage's hourly rate multiplied by how many hours have passed since entry
		fee = 1000001.99; // dummy value
	}
	
	public void pay() {
		if (!isPaid) {
			exitTime = new Date();
			isPaid = true;
		}
	}
}
