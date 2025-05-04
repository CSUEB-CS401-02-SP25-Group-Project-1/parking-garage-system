package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import server.Customer;
import server.Garage;
import server.Ticket;


public class CustomerTest {

	@Test
	public void test_customer_garage()
	{
		Garage garage = new Garage();
		Customer customer = new Customer(garage);
		
		assertEquals(garage, customer.getGarage());
		assertEquals(UserType.Customer, customer.getType());
	}
	@Test
	public void test_availbility()
	{
		String name = "Garage";
		int capacity = 100;
		double rate = 10.50;
		Garage garage = new Garage(name, capacity, rate);
		Customer customer = new Customer(garage);
		Ticket ticket = customer.generateTicket();
		assertEquals(capacity - 1, customer.availbility());
	}
}
