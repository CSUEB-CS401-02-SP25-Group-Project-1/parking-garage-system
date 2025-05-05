package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import server.Employee;
import server.Garage;
import server.Report;
import server.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import server.SecurityCamera;
import server.Server;
import server.ServerData;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerSaveTest {
    private final String DATA_DIR = Paths.get("debug", "saving_test_data").toString();
    private Server server;
    private ServerData serverData;
    private Garage garage;

    @BeforeAll
    public void setup() {
        cleanDirectory(new File(DATA_DIR)); // clean up data directory before running
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
		server = new Server(7777, "saving_test_", 
		Paths.get("debug", "logs").toString(), DATA_DIR,
		true);
		new Thread(server).start();
	}

    private String getDataFromFile(String path) {
        File dataFile = new File(path);
        waitUntilFileExists(dataFile);
        String dataString = "";
        try {
            Scanner lineScanner = new Scanner(dataFile);
            dataString = lineScanner.nextLine();
            lineScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataString;
    }

    private String getReportDataFromFile(String path) {
        File dataFile = new File(path);
        waitUntilFileExists(dataFile);
        String reportString = "";
        try {
            Scanner lineScanner = new Scanner(dataFile);
            reportString += lineScanner.nextLine()+"\n";
            reportString += lineScanner.nextLine()+"\n";
            reportString += lineScanner.nextLine();
            lineScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return reportString;
    }

    private void waitUntilFileExists(File file) {
        while (!file.exists()) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                continue;
            }
        }
    }

    private void cleanDirectory(File dir) {
        if (!dir.exists()) return;
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                cleanDirectory(file);
            } 
            file.delete();
        }
    }

    // actual tests
    @Test
    @Order(1)
    public void testSavingGarages() {
        garage = new Garage("Test Garage", 10.0, 50, 20.0);
        assertTrue(serverData.saveGarage(garage));
        assertEquals("Test Garage,10.0,50,20.0",
        getDataFromFile(Paths.get(DATA_DIR, "garages", "GA0.txt").toString()));
        assertNotNull(serverData.getGarage("GA0")); // ensure garage is added to database after saving
    }

    @Test
    public void testSavingTickets() {
        Ticket ticket = new Ticket(garage); // sets entry date
        ticket.calculateFee(); // sets exit date
        Date entryTime = ticket.getEntryTime();
        Date exitTime = ticket.getExitTime();
        ticket.overrideFee(5555.44);
        assertTrue(serverData.saveTicket(ticket));
        String expected = "GA0,"+entryTime.getTime()+","+exitTime.getTime()+",true,false,5555.44";
        assertEquals(expected, getDataFromFile(Paths.get(DATA_DIR, "tickets", "TI0.txt").toString()));
        assertNotNull(serverData.getTicket("TI0")); // ensure ticket is added to database after saving
    }

    @Test
    public void testSavingReports() {
        Report report = new Report(garage);
        report.addEntryTime(new Date(1234));
        report.addEntryTime(new Date(5678));
        report.addExit(new Date(91011), 5.0);
        report.addExit(new Date(121314), 10.0);
        assertTrue(serverData.saveReport(report));
        String expected = "GA0\n" +
                          "1234,5678\n" +
                          "91011,5.0\\|121314,10.0";
        assertEquals(expected, getReportDataFromFile(Paths.get(DATA_DIR, "reports", "RE0.txt").toString()));
        assertNotNull(serverData.getReport("RE0")); // ensure report is added to database after saving
    }

    @Test
    public void testSavingEmployees() {
        Employee employee = new Employee(garage, "my_badass_username", "VERY STRONG PASSWORD!");
        assertTrue(serverData.saveEmployee(employee));
        String expected = "GA0,my_badass_username,VERY STRONG PASSWORD!";
        assertEquals(expected, getDataFromFile(Paths.get(DATA_DIR, "employees", "EM0.txt").toString()));
        assertNotNull(serverData.getEmployee("EM0")); // ensure employee is added to database after saving
        assertNotNull(serverData.getEmployeeByUsername(garage.getID(), "my_badass_username"));
    }

    @Test
    public void testSavingCameras() {
        SecurityCamera camera = new SecurityCamera(garage);
        assertTrue(serverData.saveSecurityCamera(camera));
        String expected = "GA0";
        assertEquals(expected, getDataFromFile(Paths.get(DATA_DIR, "cameras", "SC0.txt").toString()));
        assertNotNull(serverData.getSecurityCamera("SC0")); // ensure camera is added to database after saving
    }
}