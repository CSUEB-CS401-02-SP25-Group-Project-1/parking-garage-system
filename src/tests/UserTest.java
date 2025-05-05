package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

import server.Garage;
import server.Receipt;
import server.User;
import server.UserType;

public class UserTest {

	@Test
	public void testConstructor()
	{
		User user = new User();
		assertNull(user.getGarage());
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
		User user = new User();
		user.setGarage(garage);
		assertEquals(garage,user.getGarage());
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
		assertEquals(UserType.Undefined,user.getType());
	}
	
	@Test
	public void testGetUserType()
	{
		User user = new User();
		assertEquals(UserType.Undefined,user.getType());
	}
	
	@Test
	public void testgenerateTicket()
	{
		Garage garage = new Garage("Garage Test",6.0,1,7.0);
		User user = new User(garage);
		String ticketID = user.generateTicket();
		assertNotNull(ticketID);
	}
	
	@Test
	public void testpayTicket()
	{
		Garage garage = new Garage("Garage Test",6.0,1,7.0);
		User user = new User(garage);
		String ticketID = user.generateTicket();
		assertNotNull(ticketID);
		garage.getTicket(ticketID).calculateFee();
		double amount = garage.getTicket(ticketID).getFee();
		Receipt receipt = user.payTicket(ticketID, amount);
		assertNotNull(receipt);
	}
}
