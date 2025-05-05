package tests;

import server.Receipt;
import server.Ticket;
import server.Garage;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;

public class ReceiptTest {
    
    @Test
    public void testReceiptCreationAndFormatting() {
        //1. Prepare test data (simulating a paid ticket)
        String ticketID = "T12345";
        String garageName = "Downtown Garage";
        Date entryTime = new Date(System.currentTimeMillis() - 3600000); // 1 hour ago
        Date exitTime = new Date();
        double fee = 5.50;
        
        //2. Create receipt (system would do this after payment)
        Receipt receipt = new Receipt(ticketID, garageName, entryTime, exitTime, fee);
        
        //3. Verify the receipt output contains all critical information
        String receiptText = receipt.toString();
        
        assertTrue("Receipt must show ticket ID", 
                 receiptText.contains(ticketID));
        assertTrue("Receipt must show garage name",
                 receiptText.contains(garageName));
        assertTrue("Receipt must show formatted fee",
                 receiptText.contains("$5.50") || receiptText.contains("5.50"));
        assertTrue("Receipt must show entry time",
                 receiptText.contains(entryTime.toString()));
        assertTrue("Receipt must show exit time",
                 receiptText.contains(exitTime.toString()));
    }
    
    @Test
    public void testTicketBasedReceipt() {
        //1. Create a test ticket (simulating UC-01 -> UC-03 flow)
        Garage garage = new Garage("Airport Garage", 3.0, 200, 4.0);
        Ticket ticket = new Ticket(garage);
        
        //Simulate 2 hours parking
        try { Thread.sleep(100); } catch (Exception e) {} 
        ticket.calculateFee();
        ticket.pay(ticket.getFee());
        
        //2. Generate receipt from ticket (what system does after payment)
        Receipt receipt = new Receipt(ticket);
        
        //3. Verify the receipt reflects ticket data
        String receiptText = receipt.toString();
        assertTrue(receiptText.contains(ticket.getID()));
        assertTrue(receiptText.contains(garage.getName()));
        assertTrue(receiptText.contains(String.format("%2f", ticket.getFee())));
    }
}
