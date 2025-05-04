package tests;

import server.User;
import server.Garage;
import server.Ticket;
import server.Receipt;
import server.UserType;

import org.junit.*;

public class UserTest {

	@Test
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
	}
}
