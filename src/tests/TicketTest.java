package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import server.Ticket
import server.Garage;

import java.util.Date;
import java.lang.Thread;

public class TicketTest {
	
	@Test
	public void testGetters() {
		Garage g = new Garage();
		Ticket t = new Ticket(g);

		assertEquals(t.getGarage(), g);
		assertNotNull(t.getEntryTime());

		assertNull(t.getExitTime()); // ticket is not paid off
		assertFalse(t.isPaid());
		assertFalse(t.isOverridden());
	}

	@Test
	public void testPay() {
		Ticket t = new Ticket(new Garage());

		Thread.sleep(10000); // wait so fee is > 0
				     
		t.calculateFee(); // put fee on ticket
		boolean success = t.pay(0); // fail payment
		assertFalse(success);

		success = t.pay(1000); // succeed payment
		assertTrue(success);
		assertTrue(t.isPaid());
		assertFalse(t.isOverridden());

		assertNotNull(t.getExitTime());
	}

	@Test
	public void testOverride() {
		Ticket t = new Ticket(new Garage());

		t.calculateFee();

		t.overrideFee(100); // big number
		
		assertTrue(t.isOverridden());
		assertEquals(t.getFee(), 100);
	}

	@Test
	public void testToString() {
		Garage g = new Garage();
		Ticket t = new Ticket(g);

		String expected = g.getID()
			+ "," + t.getEntryTime().getTime()
			+ ",null,false,false,0.0";

		assertEquals(expected, t.toString());
	}	
}
