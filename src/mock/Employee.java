package mock;

import java.util.Date;
import interfaces.EmployeeInterface;
import server.UserType;

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
		this.userType = UserType.Employee;
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
		// returns true if ticket was found and was overridden
		// returns false otherwise
		return false; // dummy value
	}

	public Receipt getReceipt(String ticketID) {
		// returns the receipt of a ticket
		return new Receipt("TI600", "The Awesome Garage", new Date(), new Date(), 79.99); // dummy value
	}

	public Report getGarageReport() {
		// returns a generated report of the garage
		return new Report(garage); // dummy value
	}

	public void setGarageHourlyRate(double newRate) {
		garage.setHourlyRate(newRate);
	}
	
	public String toString() {
		return garage.getID()+","+username+","+password;
	}

}
