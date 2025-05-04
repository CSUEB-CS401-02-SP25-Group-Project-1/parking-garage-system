package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import server.User;
import server.UserType;

class UserTest {

	@Test
	public void testConstructor()
	{
		User user = new User();
		assertNotNull(user.getGarage());
		assertEquals(UserType.Undefined, user.getType());
	}
	
	@Test
	public void testGarageConstructor()
	{
		Garage garage = new Garage();
		User user = new User(garage);
		assertEquals(garage,user.getGarage());
		assertEquals(UserType.Undefined,user.getType());
	}
	
	@Test
	public void testUserGarageConstructor()
	{
		Garage garage = new Garage();
		UserType userType = UserType.Employee;
		User user = new User(garage,userType);
		assertEquals(garage,user.getGarage());
		assertEquals(userType,user.getType());
	}
	
	@Test
	public void testSetGarage()
	{
		User user = new User();
		Garage garage = new Garage();
		user.setGarage(garage);
		assertEquals(garage,user.getGarage());
	}
	
	@Test
	public void testSetUserType()
	{
		User user = new User();
		UserType userType = UserType.Customer;
		user.setUserType(userType);
		assertEquals(userType,user.getType());
	}
	
	@Test
	public void testGetUserType()
	{
		User user = new User();
		assertEquals(UserType.Undefined,user.getType());
		user.setUserType(UserType.Employee);
		assertEquals(UserType.Employee,user.getType());
	}
	
	@Test
	public void testgenerateTicket()
	{
		User user = new User();
		Ticket ticket = user.generateTicket();
		assertNotNull(ticket);
	}
	
	@Test
	public void testpayTicket()
	{
		User user = new User();
		String TicketID = "Sure why not";
		double amount = 10.0;
		Receipt receipt = user.payTicket(TicketID, amount);
		assertNotNull(receipt);
	}
}
