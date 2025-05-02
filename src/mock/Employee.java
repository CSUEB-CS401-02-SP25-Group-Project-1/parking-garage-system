package mock;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import interfaces.EmployeeInterface;
import server.User;
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

	@Override
	public int getAvailableSpaces() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String viewBillingSummary(String ticketID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGarageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toggleGate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean changePassword(String newPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyGateTime(double newTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean overrideTicket(String ticketID, double newFee) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean modifyRate(double newRate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Ticket> viewActiveTickets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> viewCameraIDs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageIcon viewCameraFeed(String cameraID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> viewGarageLogs() {
		// TODO Auto-generated method stub
		return null;
	}

}
