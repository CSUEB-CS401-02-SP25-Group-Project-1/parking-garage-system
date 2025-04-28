package mock;

import java.util.Date;
import interfaces.EmployeeInterface;

public class Employee extends User implements EmployeeInterface {
	private static int count = 0;
	private String id;
	private String username;
	private String password;
	
	public Employee(Garage garage, String username, String password) {
		id = "EM"+count++;
		this.username = username;
		this.password = password;
		this.garage = garage;
	}

	public String getID() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public boolean overrideTicketFee(String ticketID, double newFee) {
		Ticket ticket = garage.getTicket(ticketID);
        	if (ticket != null && !ticket.isPaid()) {
            		ticket.overrideFee(newFee);
            		return true;
        	}
        	return false;
	}

	public Receipt getReceipt(String ticketID) {
        	Ticket ticket = garage.getTicket(ticketID);
        	if (ticket != null && ticket.isPaid()) {
            		return new Receipt(ticket.getID(), garage.getName(), 
                        	ticket.getEntryTime(), ticket.getExitTime(), 
                        	ticket.getFee());
        	}
        	return null;
    	}

	public Report getGarageReport() {
		// returns a generated report of the garage
		return garage.viewReport();
	}

	public void setGarageHourlyRate(double newRate) {
		garage.setHourlyRate(newRate);
	}

}
