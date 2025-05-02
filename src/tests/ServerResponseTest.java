package tests;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.jupiter.api.*;
import server.Server;
import shared.Message;
import shared.MessageType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerResponseTest { // tests all server responses from given client messages (sent by test employee)
	final int SERVER_PORT = 7777;
	Server server;
	Socket client;
	ObjectOutputStream clientOut;
	ObjectInputStream clientIn;
	HashMap<String, Double> fees = new HashMap<>(); // used by client to retrieve ticket fees from billings

	// setup server and client before conducting tests
	@BeforeAll
	public void setup() {
		startServer();
		server.waitUntilReady();
		startClient();
	}

	// stop client and server after testing concluded
	@AfterAll
	public void teardown() { 
		stopClient();
		server.stop();
	}

	// helper methods
	private void startServer() {
		server = new Server(SERVER_PORT, "debug_", 
		Paths.get("debug", "logs").toString(), Paths.get("debug", "data").toString(),
		false); // server data will not be saved for these tests
		new Thread(server).start();
	}

	private void startClient() {
		try {
			client = new Socket("localhost", SERVER_PORT);
			clientOut = new ObjectOutputStream(client.getOutputStream());
			clientIn = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void stopClient() {
		try {
			clientOut.close();
			clientIn.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(String text) { // sends message to server from client
		Message message = new Message(MessageType.Request, "testEmployee", text);
		try {
			clientOut.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Message getMessage() { // client waits for message from server
		Message message = null;
		try {
			message = (Message)clientIn.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	private boolean isPayTicketFailMessage(Message msg) {
		if (msg.getText().equals("pt:ticket_not_found") ||
			msg.getText().equals("pt:already_paid")) {
				return true;
			}
		return false;
	} 

	private String getBilling(String ticketID) {
		sendMessage("bs:"+ticketID);
		return getMessage().getText();
	}

	private double getFeeFromBilling(String billingSummary) {
		String split[] = billingSummary.split(":");
		return Double.parseDouble(split[4]);
	}

	// actual tests
	@Test
	@Order(1) // this is the first test (needed for server to identify client)
	public void InitTest() {
		// server returns "invalid init" message if client sends invalid parameters
		sendMessage("foo:bar");
		Message response = getMessage();
		assertEquals("init:invalid", response.getText());

		// server returns "nonexistent garage" message if their requested garage is not in database
		sendMessage("init:GA999:em");
		response = getMessage();
		assertEquals("init:nonexistent_garage", response.getText());

		// server assigns client their requested role if their requested garage exists
		sendMessage("init:GA0:em");
		response = getMessage();
		assertEquals("init:assigned_em", response.getText());
	}
	
	@Test
	@Order(2) // second test (needed to sign in after init)
	public void EmployeeLoginTest() {
		// server sends "invalid credentials" message if client's provided credentials do not match those in database
		sendMessage("li:asdf:ghjk");
		Message response = getMessage();
		assertEquals("li:invalid_credentials", response.getText());

		sendMessage("li:test_employee:ghjk"); // with correct username
		response = getMessage();
		assertEquals("li:invalid_credentials", response.getText());

		// server sends "success" response if client's credentials match those in database
		sendMessage("li:test_employee:test_password");
		response = getMessage();
		assertEquals("li:successful", response.getText());
	}

	@Test
	@Order(3)
	public void ViewBillingSummaryTest() {
		// server sends the billing information of a ticket based off of a provided ticket id
		String response = getBilling("TI0");
		assertEquals("bs:TI0:1234:5678:150.0", response);

		// able to get expected ticket fee from billing info
		double fee = getFeeFromBilling(response);
		assertTrue(150.00 == fee);
		fees.put("TI0", fee); // used for test #5 to pay tickets with correct payment amount

		// server sends "invalid ticket id" response if client requests for ticket not in database
		response = getBilling("TI999");;
		assertEquals("bs:invalid_ticket_id", response);
	}

	@Test
	@Order(4)
	public void GenerateTicketTest() {
		// server sends ticket id of generated ticket when successful
		Message response;
		for (int i = 1; i < 30; i++) { // test garage holds up to 30 cars (TI0 is already loaded before test)
			sendMessage("gt");
			response = getMessage();
			assertEquals("gt:TI"+i, response.getText());
			fees.put("TI"+i, getFeeFromBilling(getBilling("TI"+i))); // used for next test to pay fees of a correct amount
		}

		// server sends "unavailable space" message when client requests to generate ticket in a full garage
		sendMessage("gt");
		response = getMessage();
		assertEquals("gt:unavailable_space", response.getText());
	}

	@Test
	@Order(5)
	public void PayTicketTest() {
		// server sends human-readable receipt when successful
		Message response;
		for (int i = 1; i < 15; i++) { // TI0 will be tested seperately
			String ticketID = "TI"+i;
			sendMessage("pt:"+ticketID+":"+fees.get(ticketID));
			response = getMessage();
			assertTrue(response.getText().startsWith("pt:") && !isPayTicketFailMessage(response));
		}

		// server sends error message when ticket is invalid

		// ticket has already been paid for
		sendMessage("pt:TI1:"+fees.get("TI1"));
		response = getMessage();
		assertEquals("pt:already_paid", response.getText());

		// server sends "incorrect amount" message if inputted payment amount is invalid
		sendMessage("pt:TI0:666.66"); // testing with TI0 now
		response = getMessage();
		assertEquals("pt:incorrect_amount", response.getText());

		// ticket not found in database
		sendMessage("pt:TI999:9999.99");
		response = getMessage();
		assertEquals("pt:ticket_not_found", response.getText());
	}
	
	@Test
	@Order(6)
	public void ViewAvailableSpacesTest() {
		// 
	}
	
	@Test
	public void ToggleGateTest() {
		
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
