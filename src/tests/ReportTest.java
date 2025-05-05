package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;
import org.junit.Before;

import server.Garage;
import server.Earning;
import server.Report;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class ReportTest {
    private List<Date> expectedEntries;
    private List<Earning> expectedEarnings;

    @Before
    public void setup() {
        expectedEntries = new ArrayList<>();
        expectedEarnings = new ArrayList<>();

        for (int i=0; i<5; i++) {
        	Date now = new Date();
        	expectedEntries.add(now);

        	Earning e = new Earning(now, 20.00);
        	expectedEarnings.add(e);
        }
    }

    @Test
    public void testGetters() {
		Garage g = new Garage();
		Report r = new Report(g);
	
		assertNotNull(r.getID());
		assertEquals(g, r.getGarage());
		assertEquals(r.getCurrentlyParkedNum(), 0);
	
		for (Date d : expectedEntries) {
		    r.addEntryTime(d);
		}	
	
		for (Earning e : expectedEarnings) {
		    r.addExit(e);
		}
		
		assertEquals(expectedEntries, r.getEntryTimes());
		assertEquals(expectedEarnings, r.getEarnings());
    }

    @Test
    public void testAccumulators() {
		Report r = new Report();
		for (Date d : expectedEntries) {
			r.addEntryTime(d);
		}
	    for (Earning e : expectedEarnings) {
		    r.addExit(e);
		}
		// 5 exits all give $20.00
		double expected_revenue = 5 * expectedEarnings.get(0).getRevenue();
		assertEquals(expected_revenue, r.getRevenueThisHour());
		assertEquals(expected_revenue, r.getRevenueToday());
		assertEquals(expected_revenue, r.getRevenueThisWeek());
		assertEquals(expected_revenue, r.getRevenueThisMonth());
		assertEquals(expected_revenue, r.getRevenueThisYear());
		// etc...
	
		String peakHour = "" + expectedEntries.get(0).getHours();
		assertEquals(peakHour, r.getPeakHour());
    }

    @Test
    public void testToString() {
		Garage g = new Garage();
		Report r = new Report(g);
	
		String expected = "";
		String entries_s = "";
		String exits_s = "";
	
		expected += g.getID() + "\n\n\n";
		// test adding no entries or exits
		assertEquals(expected, r.toString());
	
		for (Date d : expectedEntries) {
		    r.addEntryTime(d);
		    entries_s += d.getTime() + ",";
		}
	
		for (Earning e : expectedEarnings) {
		    r.addExit(e);
		    exits_s += e.toString() + "\\|";
		}
	
		expected = g.getID() + "\n" + entries_s + "\n" + exits_s + "\n";
		assertEquals(expected, r.toString());
    }
}