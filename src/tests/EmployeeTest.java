package tests;

import server.Employee;
import server.Garage;
import server.Report;
import static org.junit.Assert.*;
import org.junit.*;

public class EmployeeTest {
	@Test
	public void testChangePassword() {
		Employee employee = new Employee(new Garage(), "tester", "changeme!");
		
		//Attempts an invalid password, success if password is NOT changed 
		employee.changePassword("password123");
		assertTrue(employee.getPassword() != "password123");
		
		//Attempts a valid password, success if password IS changed
		employee.changePassword("myNewPass#");
		assertTrue(employee.getPassword() == "myNewPass#");
	}
	
	@Test
	public void testModifyGateTime() {
		Employee employee = new Employee(new Garage(), "tester", "changeme!");
		
		//Attempts an invalid gate time, success if gate time NOT changed 
		employee.modifyGateTime(-5);
		assertTrue(employee.getGarage().getGate().getOpenTime() != -5);
		
		//Attempts a valid gate time, success if gate time IS changed
		employee.modifyGateTime(15);
		assertTrue(employee.getGarage().getGate().getOpenTime() == 15);
	}
	
	@Test
	public void testOverrideTicket() {
		Garage garage = new Garage("tester", 1, 1, 1);
		Employee employee = new Employee(garage, "tester", "changeme!");
		String ticketID = garage.generateTicket();
		
		//Attempts an invalid fare override on the only ticket in the garage
		//Success if the ticket fare does NOT change
		employee.overrideTicket(ticketID, -5);
		assertTrue(garage.getTicket(ticketID).getFee() != -5);
		
		//Attempts valid fare override on the only ticket in the garage
		//Success if the ticket fare DOES change
		employee.overrideTicket(ticketID, 500);
		assertTrue(garage.getTicket(ticketID).getFee() == 500);
	}
	
	@Test
	public void testModifyRate() {
		Garage garage = new Garage("tester", 1, 1, 1);
		Employee employee = new Employee(garage, "tester", "changeme!");
		
		//Attempts an invalid rate modification, success if NOT changed
		employee.modifyRate(-5);
		assertTrue(garage.getHourlyRate() != -5);
		
		//Attempts a valid rate modification, success if IS changed
		employee.modifyRate(5);
		assertTrue(garage.getHourlyRate() == 5);
	}
	
	@Test
	public void testGetReceipt() {
		Garage garage = new Garage("tester", 1, 50, 1);
		Employee employee = new Employee(garage, "tester", "changeme!");
		String ticket1_ID = garage.generateTicket();
		String ticket2_ID = garage.generateTicket();
		garage.getTicket(ticket2_ID).pay(garage.getTicket(ticket2_ID).getFee());
		
		//Attempts to get a receipt from a ticket that is NOT already paid
		//Success if NO RECEIPT is returned
		assertTrue(employee.getReceipt(ticket1_ID) == null);
		
		//Attempts to get a receipt from a ticket that HAS already been paid
		//Success if a receipt IS returned
		assertTrue(employee
				.getReceipt(ticket2_ID)
				.getClass()
				.getSimpleName()
				.equals("Receipt"));

	}
}
