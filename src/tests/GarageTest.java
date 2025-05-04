package tests;

import server.*;
import org.junit.*;

public class GarageTest {
	
	@Test
	public void testSetters() {
		String name = "name";
		int capacity = 200;
		double rate = 20.25;
		Garage garage1;
		garage1.setName(name);
		garage1.setCapacity(capacity);
		garage1.setHourlyRate(rate);

		Garage garage2 = new Garage(name, capacity, rate);
	
		// if ids are unequal
		assertTrue(garage1.getID().compareTo(garage2.getID()) != 0);

		// if setter set name properly
		assertEquals(name, garage1.getName());
		assertEquals(name, garage2.getName());

		// if setter set capacity properly
		assertEquals(capacity, garage1.getCapacity());
		assertEquals(capacity, garage2.getCapacity());

		// if setter set rate properly
		assertEquals(rate, garage1.getHourlyRate());
		assertEquals(rate, garage2.getHourlyRate());
	}

	@Test
	public void testTickets() {
		String name = "name";
		int capacity = 20;
		double rate = 10.50;
		Garage garage = new Garage(name, capacity, rate);

		List<Ticket> buffer = new ArrayList<>();
		
		// fill garage with tickets
		// catch returned tickets in a buffer to test more getters
		for (int i = 0; i < capacity; i++) {
			buffer.add(garage.generateTicket());
		}

		// if any tickets have been added at all
		assertTrue(garage.getTicket("T0") != null);

		// test if garage has been filled
		assertTrue(garage.isFull());

		// if the returned ticket is accurate
		assertEquals(buffer.get(0), garage.getTicket("T0"));
		// and a few others
		assertEquals(buffer.get(5), garage.getTicket("T5"));
		assertEquals(buffer.get(10), garage.getTicket("T10"));
		assertEquals(buffer.get(15), garage.getTicket("T15"));


		// test payTicket() by paying off every ticket
		for (Ticket ticket : garage.getTickets()) {
			garage.payTicket(ticket.getID(), 4000); // big number
		}

		// test if garage is no longer full
		assertTrue(!garage.isFull());
	}
}
