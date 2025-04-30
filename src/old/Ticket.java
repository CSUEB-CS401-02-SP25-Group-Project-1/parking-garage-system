package server;

import java.util.Date;

public class Ticket {
	private static int count = 0;

	private Garage garage;
	private Date entryTime;
	private Date exitTime;
	private boolean isClosed;
	private boolean isOverridden;
	private int ID;

	public Ticket(Garage garage) {
		entryTime = new Date();
		exitTime = null;
		ID = ++count;

		isClosed = false;
		isOverridden = false;

		this.garage = garage;
	}

	public Garage getGarage() {return garage;}
	public Date getEntryTime() {return entryTime;}
	public Date getExitTime() {
		// if the ticket is not closed, there is no exit time
		if (!isClosed) {return null;}
		else {return exitTime;}
	}
	public boolean isClosed() {return isClosed;}
	public boolean isOverridden() {return isOverridden;}
	public String getID() {return ("T" + ID);}
	
	public void override() {
		// force-pays a ticket
		isClosed = true;
		isOverridden = true;
		exitTime = new Date();
	}	

	public boolean pay(float amount) {
		Date now = new Date();
		long hours_in_garage = 
			(now.getTime() - entryTime.getTime()) / 3600000;
		// get rate from garage reference
		float due = hour_in_garage * garage.getRate();
		if (amount >= due) {
			exitTime = now;
			isClosed = true;
			return true;
		} else {
			return false;
		}
	}
}
