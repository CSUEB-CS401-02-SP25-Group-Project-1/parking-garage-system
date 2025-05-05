package tests;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import server.Report;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import server.Earning;
import server.Server;
import server.ServerData;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerLoadTest {
    private Server server;
    private ServerData serverData;

    @BeforeAll
    public void setup() {
        startServer();
        server.waitUntilReady();
        serverData = server.getServerData();
    }

    @AfterAll
	public void teardown() { 
		server.stop();
	}

    // helper methods
	private void startServer() {
		server = new Server(7777, "loading_test_", 
		Paths.get("debug", "logs").toString(), Paths.get("debug", "loading_test_data").toString(),
		false); // server data will not be saved for these tests
		new Thread(server).start();
	}
    
    // actual tests
    @Test
    public void testLoadedGarages() {
        assertNotNull(serverData.getGarage("GA0")); // GA0 is valid
        assertNull(serverData.getGarage("GA1")); // GA1 is invalid (shouldn't have loaded)
    }

    @Test
    public void testLoadedTickets() {
        assertNotNull(serverData.getTicket("TI0")); // TI0 is valid
        assertNotNull(serverData.getTicket("TI1")); // TI1 is valid (null values are accepted for date times and final fee)
        assertNull(serverData.getTicket("TI2")); // TI2 is invalid (shouldn't have loaded)
    }

    @Test
    public void testLoadedReports() {
        Report validReport = serverData.getReport("RE0");
        assertNotNull(validReport); // RE0 is valid
        assertFalse(validReport.getEntryTimes().size() == 0); // RE0's entry times should've been loaded
        assertFalse(validReport.getEarnings().size() == 0); // RE0's entry times should've been loaded

        Report invalidReport = serverData.getReport("RE1");
        assertNotNull(invalidReport); // RE1 is invalid (object will still be created by design but is supposed to be empty)
        assertTrue(invalidReport.getEntryTimes().size() == 0); // entries list should be empty
        assertTrue(invalidReport.getEarnings().size() == 0); // earnings list should be empty

        Report validReportWithInvalidData = serverData.getReport("RE2");
        assertNotNull(validReportWithInvalidData); // RE2 should load
        int validTimes[] = {0, 1, 2, 3, 5}; // entry times that should have loaded
        ArrayList<Date> loadedTimes = validReportWithInvalidData.getEntryTimes();
        for (int i = 0; i < validTimes.length; i++) {
            assertEquals(validTimes[i], loadedTimes.get(i).getTime());
        }
        Earning validEarnings[] = {new Earning(new Date(0), 0.0), 
                                   new Earning(new Date(1), 1.1),
                                   new Earning(new Date(4), 4.4),
                                   new Earning(new Date(5), 5.5),
                                   new Earning(new Date(6), 6.6)};
        ArrayList<Earning> loadedEarnings = validReportWithInvalidData.getEarnings();
        for (int i = 0; i < validEarnings.length; i++) {
            assertEquals(validEarnings[i].toString(), loadedEarnings.get(i).toString());
        }
    }

    @Test
    public void testLoadedEmployees() {
        assertNotNull(serverData.getEmployee("EM0")); // EM0 is valid
        assertNull(serverData.getEmployee("EM1")); // EM1 is invalid (shouldn't have loaded)
    }

    @Test
    public void testLoadedCameras() {
        assertNotNull(serverData.getSecurityCamera("SC0")); // SC0 is valid
        assertNull(serverData.getSecurityCamera("SC1")); // SC1 is invalid (shouldn't have loaded)
    }
}
