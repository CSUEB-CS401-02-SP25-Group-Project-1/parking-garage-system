package tests;

import mock.*;

import org.junit.*;

public class UserTest {

	@Test
	public void testSetters() {
		Garage garage = new Garage(); // no parameters for mock garage
		UserType type = UserType.Customer;
		User user1;
		user1.setGarage(garage);
		user1.setType(type);

		assertEquals(garage, user1.getGarage());
		assertEquals(type, user1.getType());
	}

	@Test
	public void testTickets() {
		User user = new User();
		// add a ticket
		Ticket t = user.generateTicket();

		// ticket will exist if generated
		assertTrue(t != null);

		// pay the ticket off
		Receipt r = user.payTicket(t.getID(), 4000);
		
		// receipt will exist if ticket was paid
		assertTrue(r != null);

		// try to pay off ticket again
		r = user.payTicket(t.getID(), 4000);

		// `r` will be null because ticket is already removed
		assertEquals(r, null);
	}
}
