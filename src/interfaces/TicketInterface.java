package interfaces;

import java.util.Date;
import server.Garage;

public interface TicketInterface {
	Garage getGarage();
	Date getEntryTime();
	Date getExitTime();
	boolean isOverridden();
	boolean isPaid();
	String getID();
	void overrideFee(double newFee);
	void calculateFee();
	double getFee();
	boolean pay(double paymentAmount);
}
