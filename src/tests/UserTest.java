package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import server.Garage;
import server.Receipt;
<<<<<<< HEAD
=======
import server.User;
>>>>>>> 427e3c7aa43c711fc5935e03a9f9d4428d03784b
import server.UserType;

class UserTest {

	@Test
<<<<<<< HEAD
	public void testSetters() {
		Garage garage = new Garage(); // no parameters for mock garage
		UserType type = UserType.Customer;
		User user1 = new User();
		user1.setGarage(garage);
		user1.setType(type);

		assertEquals(garage, user1.getGarage());
		assertEquals(type, user1.getType());
	}

	@Test
	public void testTickets() {
		User user = new User();
		// add a ticket
		String s = user.generateTicket();
		Ticket t = user.getGarage().getTicket(s);

		// ticket will exist if generated
		assertNotNull(t);

		// pay the ticket off
		Receipt r = user.payTicket(t.getID(), 4000);
		
		// receipt will exist if ticket was paid
		assertNotNull(r);

		// try to pay off ticket again
		r = user.payTicket(t.getID(), 4000);

		// `r` will be null because ticket is already removed
		assertNull(r);
=======
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
>>>>>>> 427e3c7aa43c711fc5935e03a9f9d4428d03784b
	}
}
