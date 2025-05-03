package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class GarageTest {
    private Garage garage;
    private final String GARAGE_NAME = "Test Garage";
    private final double HOURLY_RATE = 5.0;
    private final int CAPACITY = 10;
    private final double GATE_OPEN_TIME = 3.0;

    @Before
    public void setUp() {
        garage = new Garage(GARAGE_NAME, HOURLY_RATE, CAPACITY, GATE_OPEN_TIME);
    }

    @Test
    public void testConstructor() {
        assertNotNull(garage);
        assertEquals(GARAGE_NAME, garage.getName());
        assertEquals(HOURLY_RATE, garage.getHourlyRate(), 0.001);
        assertEquals(CAPACITY, garage.getCapacity());
        assertNotNull(garage.getGate());
        assertTrue(garage.getID().startsWith("GA"));
    }

    @Test
    public void testDefaultConstructor() {
        Garage defaultGarage = new Garage();
        assertNotNull(defaultGarage);
        assertEquals("Unnamed", defaultGarage.getName());
        assertEquals(0.0, defaultGarage.getHourlyRate(), 0.001);
        assertEquals(0, defaultGarage.getCapacity());
        assertNotNull(defaultGarage.getGate());
    }

    @Test
    public void testGetAvailableSpaces() {
        assertEquals(CAPACITY, garage.getAvailableSpaces());
        
        garage.generateTicket();
        assertEquals(CAPACITY - 1, garage.getAvailableSpaces());
    }

    @Test
    public void testIsFull() {
        assertFalse(garage.isFull());
        
        // Fill the garage
        for (int i = 0; i < CAPACITY; i++) {
            garage.generateTicket();
        }
        
        assertTrue(garage.isFull());
        assertEquals(0, garage.getAvailableSpaces());
    }

    @Test
    public void testGenerateTicket() {
        String ticketId = garage.generateTicket();
        assertNotNull(ticketId);
        assertEquals(1, garage.getActiveTickets().size());
        assertEquals(1, garage.getAllTickets().size());
        
        // Test generating ticket when garage is full
        for (int i = 1; i < CAPACITY; i++) {
            garage.generateTicket();
        }
        assertNull(garage.generateTicket()); // should return null when full
    }

    @Test
    public void testLoadTicket() {
        Ticket ticket = new Ticket(garage);
        garage.loadTicket(ticket);
        
        assertEquals(1, garage.getActiveTickets().size());
        assertEquals(1, garage.getAllTickets().size());
        
        // Test loading a paid ticket
        Ticket paidTicket = new Ticket(garage);
        paidTicket.pay(10.0);
        garage.loadTicket(paidTicket);
        
        assertEquals(1, garage.getActiveTickets().size()); // shouldn't be added to active
        assertEquals(2, garage.getAllTickets().size()); // but should be in all tickets
    }

    @Test
    public void testGetTicket() {
        String ticketId = garage.generateTicket();
        Ticket foundTicket = garage.getTicket(ticketId);
        
        assertNotNull(foundTicket);
        assertEquals(ticketId, foundTicket.getID());
        
        assertNull(garage.getTicket("NON_EXISTENT_ID"));
    }

    @Test
    public void testPayTicket() {
        // 1. First generate a valid ticket
        String ticketId = garage.generateTicket();
        assertNotNull(ticketId);  // Verify ticket was created
        
        // 2. Get the ticket and verify it exists
        Ticket ticket = garage.getTicket(ticketId);
        assertNotNull(ticket);  // This would fail if getTicket() is broken
        
        // 3. Now attempt payment
        Receipt receipt = garage.payTicket(ticketId, 10.0);
        assertNotNull(receipt);
    }

    @Test
    public void testCameraOperations() {
        SecurityCamera camera = new SecurityCamera(garage);

        // Test adding camera
        assertTrue(garage.addCamera(camera));
        assertEquals(1, garage.getCameras().size());

        // Test adding duplicate camera
        assertFalse(garage.addCamera(camera));  // Should return false for duplicate
        assertEquals(1, garage.getCameras().size());  // Size shouldn't change

        // Test getting camera
        assertNotNull(garage.getCamera(camera.getID()));
        assertNull(garage.getCamera("NON_EXISTENT_ID"));
        
        // Test removing camera
        assertTrue(garage.removeCamera(camera.getID()));
        assertEquals(0, garage.getCameras().size());
        
        // Test removing non-existent camera
        assertFalse(garage.removeCamera("NON_EXISTENT_ID"));
    }

    @Test
    public void testLogOperations() {
        String logEntry = "Test log entry";
        garage.addLogEntry(logEntry);
        
        ArrayList<String> logs = garage.getLogEntries();
        assertEquals(1, logs.size());
        assertEquals(logEntry, logs.get(0));
    }

    @Test
    public void testGateOperations() {
        Gate gate = garage.getGate();
        assertNotNull(gate);
        assertEquals(GATE_OPEN_TIME, gate.getOpenTime(), 0.001);
        
        // Test gate state changes
        assertFalse(gate.isOpen());
        gate.open();
        assertTrue(gate.isOpen());
        gate.close();
        assertFalse(gate.isOpen());
    }

    @Test
    public void testSetters() {
        String newName = "New Name";
        garage.setName(newName);
        assertEquals(newName, garage.getName());
        
        double newRate = 7.5;
        garage.setHourlyRate(newRate);
        assertEquals(newRate, garage.getHourlyRate(), 0.001);
    }

    @Test
    public void testReportHandling() {
        Report report = new Report(garage);
        garage.loadReport(report);
        assertSame(report, garage.viewReport());
    }

    @Test
    public void testToString() {
        String expected = GARAGE_NAME + "," + HOURLY_RATE + "," + CAPACITY + "," + GATE_OPEN_TIME;
        assertEquals(expected, garage.toString());
    }
}
