package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

import server.Gate;
import server.Garage;

public class GateTest {
    // test constructor
    // test getters
	
    @Test
    public void testGate() {
	Garage g = new Garage();
	double openTime = 20.0;
        Gate gate = new Gate(g, openTime);
	
	assertEquals(g, gate.getGarage());
	assertEquals(openTime, gate.getOpenTime());

	openTime = 25.0;
	gate.setOpenTime(openTime);
	assertEquals(openTime, get.getOpenTime());

	gate.close();
	assertFalse(gate.isOpen());

	gate.open();
	assertTrue(gate.isOpen());
    }
}
