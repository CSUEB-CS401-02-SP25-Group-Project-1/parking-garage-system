package tests;

import static org.junit.Assert.*;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;
import server.Server;

public class ServerResponseTest { // tests all server responses from given client messages (sent by test employee)
	Server server;

	// setup server and client before conducting tests
	@BeforeEach
	public void setup() {
		startServer();
		server.waitUntilReady();
		// TODO: setup client here
	}

	// helper methods for setup
	private void startServer() {
		server = new Server(7777, "debug_", 
		Paths.get("debug", "logs").toString(), Paths.get("debug", "data").toString(),
		false); // server data will not be saved for these tests
		new Thread(server).start();
	}

	// stop server after testing concluded
	@AfterEach
	public void stopServer() { 
		server.stop();
	}

	// actual tests
	@Test
	@Order(1) // this is the first test (needed for server to identify client)
	public void InitTest() {
		
	}
	
	@Test
	@Order(2) // second test (needed to sign in after init)
	public void EmployeeLoginTest() {
		
	}
	
	@Test
	public void GenerateTicketTest() {
		
	}
	
	@Test
	public void ViewAvailableSpacesTest() {
		
	}
	
	@Test
	public void PayTicketTest() {
		
	}
	
	@Test
	public void ToggleGateTest() {
		
	}
	
	@Test
	public void ViewBillingSummaryTest() {
		
	}
	
	@Test
	public void GetGarageNameTest() {
		
	}
	
	@Test
	public void ChangePasswordTest() {
		
	}
	
	@Test
	public void ModifyGateOpenTimeTest() {
		
	}
	
	@Test
	public void OverrideTicketTest() {
		
	}
	
	@Test
	public void ViewReportTest() {
		
	}
	
	@Test
	public void ModifyRateTest() {
		
	}
	
	@Test
	public void ViewActiveTicketsTest() {
		
	}
	
	@Test
	public void ViewCameraIDsTest() {
		
	}
	
	@Test
	public void ViewGarageLogsTest() {
		
	}
	
	@Test
	@Order(Integer.MAX_VALUE) // last test
	public void LogoutTest() {
		
	}
	
}
