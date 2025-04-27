package interfaces;

import server.Receipt;
import server.Report;

public interface EmployeeInterface {
	String getID();
	String getUsername();
	String getPassword();
	void setPassword(String newPassword);
	boolean overrideTicketFee(String ticketID, double newFee); // returns true if successful (ticket was found and not paid-for yet)
	Receipt getReceipt(String ticketID); // returns receipt of an already paid-for ticket
	Report getGarageReport();
	void setGarageHourlyRate(double newRate);
}
