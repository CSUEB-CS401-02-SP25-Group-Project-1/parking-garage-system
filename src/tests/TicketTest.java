package tests;

import static org.junit.Assert.*;

import org.junit.*;
import server.Ticket;
import server.Garage;
import java.lang.Thread;
import java.util.Date;

public class TicketTest {
	
	@Test
	public void testGetters() {
		Garage garage = new Garage("Test_Garage", 5, 30, 1);
		Ticket ticket = new Ticket(garage);

		//Confirming garage is set correctly
		assertEquals(ticket.getGarage(), garage);
		
		//Confirming ticket entry time is not null
		assertNotNull(ticket.getEntryTime());

		//Attempting to get the exit time on an UNPAID ticket,
		//should return null
		assertNull(ticket.getExitTime());
		
		//Confirming that ticket is yet to be paid
		assertFalse(ticket.isPaid());
		
		//Confirming that the ticket's fee hasn't been overridden 
		assertFalse(ticket.isOverridden());
	}

	@Test
	public void testPay() throws InterruptedException {
		Garage garage = new Garage("Test_Garage", 50000, 30, 1);
		Ticket ticket = new Ticket(garage);
		
		//After creating ticket, give it time to accumulate a fee
		Thread.sleep(10000);
		ticket.calculateFee();
		double fee = ticket.getFee();
		
		//Confirming that the ticket CANNOT be paid for less than it's fee
		assertFalse(ticket.pay(fee - 10));

		//Confirming that the ticket CAN be paid for it's fee
		assertTrue(ticket.pay(ticket.getFee()));
		
		//Confirm that ticket has been marked as paid
		assertTrue(ticket.isPaid());
		
		//Confirm that the ticket still has NOT been overridden
		assertFalse(ticket.isOverridden());

		//Confirm that the ticket's exit time is NOT still null after payment
		assertNotNull(ticket.getExitTime());
	}

	@Test
	public void testOverride() {
		Garage garage = new Garage("Test_Garage", 5, 30, 1);
		Ticket ticket = new Ticket(garage);

		//Calculating the ticket's current fee, 
		//then overriding with a different fee
		ticket.calculateFee();
		ticket.overrideFee(1738);
		
		//Confirming that the ticket has been overridden
		assertTrue(ticket.isOverridden());
		
		//Confirming that the ticket's fee has been changed successfully
		assertTrue(ticket.getFee() == 1738);
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
	
	@Test
	public void testCorreectFeeCalculation() {
		//Created a garage with an hourly rate of $50,000/hr
		Garage garage = new Garage("Test_Garage", 50000, 30, 1);
		
		//Creating a ticket with an entry time
		// exactly 30 min ago
		Date thirtyMinAgo = new Date();
		thirtyMinAgo.setMinutes(thirtyMinAgo.getMinutes() - 30);
		Ticket ticket = new Ticket(garage, thirtyMinAgo, new Date(), false, false, 0.0);
		
		//Calculate the fee on the ticket
		ticket.calculateFee();
		
		//Confirm that the fee is 30 min of parking fee ($25k)
		assertTrue((int)ticket.getFee() == 25000);
	}
}
