package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import server.Customer;
import server.Garage;
import server.Ticket;
import server.UserType

public class CustomerTest 
{

	@Test
	public void testCustomerGarageConstructor()
	{
		Garage garage = new Garage();
		Customer customer = new Customer(garage);
		assertSame("Garage Testing and Customer Testing",garage,customer.getGarage());
		assertTrue("Customer being type Customer",customer.getType() == UserType.Customer);
	}
	
	@Test
	public void testviewGarageAvailability()
	{
		int Spots = 200;
		Garage garage_spots = new Garage("Best Garage",Spots,10);
	    Customer customer_user = new Customer(garage_spots);
		Ticket ticket = customer_user.generateTicket();
		int Availabity = Spots - 1;
		assertEquals("After issuing one ticket, the availabity spots will decrease",Availabity,customer_user.viewGarageAvailability());
	}
}
