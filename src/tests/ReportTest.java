package tests;

import server.Garage;
import server.Earning;
import server.Report;

import java.util.Date;
import java.util.ArrayList;

public class ReportTest {
    private List<Date> expectedEntries;
    private List<Earning> expectedEarnings;

    @BeforeAll
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
	    r.addEarning(e);
	}
	
	assertEquals(expectedEntries, r.getEntrytimes());
	assertEquals(expectedEarnings, r.getEarnings());

	assertTrue(r.getCurrentlyParkedNum() > 0);
    }

    @Test
    public void testAccumulators() {
	Report r = new Report();
        for (Earning e : expectedEarnings) {
	    r.addExit(e);
	}
	// 5 exits all give $20.00
	double revenue = 5 * 20.00;
	assertEquals(revenue, r.getRevenueThisHour());
	assertEquals(revenue, r.getRevenueToday());
	// etc...

	Date peakHour = expectedEntries.get(0); 
	String expected = "" + peakHour;
	assertEquals(peakHour, r.getPeakHour());
    }

    @Test
    public void testToString() {
	Garage g = newGarage();
	Report r = newReport(g);

	String expected = "";
	String entries_s = "";
	String exits_s = "";

	expected += g.getID() + "\n\n";
	// test adding no entries or exits
	assertEquals(expected, r.toString());

	for (Date d : expectedEntries) {
	    r.addEntry(d);
	    entries_s += d.getTime() + ",";
	}

	for (Earning e : expectedEarnings) {
	    r.addExit(e);
	    exits_s += e.toString();
	}

	expected = g.getID() + "\n" + entries_s + "\n" + exits_s;
	assertEquals(expected, r.toString());
    }
}
