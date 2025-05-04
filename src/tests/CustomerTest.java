package tests;

import server.*;
import org.junit.*;

public class CustomerTest {
	@Test
	public void testInheritance() {
		Garage g = new Garage();
		Customer customer = new Customer(g);
		
		// test inheritance
		assertEquals(g, customer.getGarage());
		assertEquals(UserType.Customer, customer.getType());
	}

	@Test
	public void testViewGarageAvailablity() {
		String name = "name";
		int capacity = 20;
		double rate = 10.50;
		Garage g = new Garage(name, capacity, rate);
		Customer customer = new Customer(g);

		// add ticket to garage
		Ticket t = customer.generateTicket();
		
		// One ticket added means available space is (capacity - 1)
		assertEquals(capacity - 1, customer.viewGarageAvailability());
	}
}
