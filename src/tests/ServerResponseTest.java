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
import shared.ImageMessage;
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
		server = new Server(SERVER_PORT, "response_test_", 
		Paths.get("debug", "logs").toString(), Paths.get("debug", "response_test_data").toString(),
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

	private ImageMessage getImageMessage() {
		ImageMessage message = null;
		try {
			message = (ImageMessage)clientIn.readObject();
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

		sendMessage("li:employee_in_another_garage:foo"); // with correct credentials from wrong garage
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

		// server should not let client get billing of a ticket from another garage
		response = getBilling("TI666");
		assertEquals("bs:invalid_ticket_id", response);
	}

	@Test
	@Order(4)
	public void GenerateTicketTest() {
		// server sends ticket id of generated ticket when successful
		Message response;
		for (int i = 2; i <= 30; i++) { // test garage holds up to 30 cars (TI0 and TI1 are already loaded before test)
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
		for (int i = 2; i < 16; i++) { // TI0 will be tested seperately
			String ticketID = "TI"+i;
			sendMessage("pt:"+ticketID+":"+fees.get(ticketID));
			response = getMessage();
			assertTrue(response.getText().startsWith("pt:") && !isPayTicketFailMessage(response));
		}

		// server sends error message when ticket is invalid

		// ticket has already been paid for
		sendMessage("pt:TI2:"+fees.get("TI2"));
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

		// server should not let client pay for another ticket that's in another garage
		sendMessage("pt:TI1:666.66");
		response = getMessage();
		assertEquals("pt:ticket_not_found", response.getText());
	}
	
	@Test
	@Order(6)
	public void ViewAvailableSpacesTest() {
		// available spaces should return 14 since there's still 14 unpaid (active) tickets in garage
		sendMessage("va");
		Message response = getMessage();
		assertEquals("va:14", response.getText());
	}
	
	@Test
	public void ToggleGateTest() {
		// server should send back message acknowledging client's request
		sendMessage("tg");
		Message response = getMessage();
		assertEquals("tg:toggled", response.getText());
	}
	
	@Test
	public void GetGarageNameTest() {
		// server should return human-readable name of garage
		sendMessage("gn");
		Message response = getMessage();
		assertEquals("gn:Test Garage", response.getText());
	}
	
	@Test
	public void ChangePasswordTest() {
		// server should accept passwords containing special characters
		sendMessage("mp:*(accept_me_i_contain_special_characters_&_i_love_$paghetti)*");
		Message response = getMessage();
		assertEquals("mp:password_changed", response.getText());

		// server should reject passwords with no special characters
		sendMessage("mp:rejectmeimnotspecial");
		response = getMessage();
		assertEquals("mp:no_special_characters", response.getText());
	}
	
	@Test
	public void ModifyGateOpenTimeTest() {
		// server should accept integers
		sendMessage("mg:60");
		Message response = getMessage();
		assertEquals("mg:time_updated", response.getText());

		// server should also accept floating point numbers
		sendMessage("mg:17.5");
		response = getMessage();
		assertEquals("mg:time_updated", response.getText());

		// server should reject negative values
		sendMessage("mg:-5");
		response = getMessage();
		assertEquals("mg:invalid_time", response.getText());

		sendMessage("mg:-5.55");
		response = getMessage();
		assertEquals("mg:invalid_time", response.getText());
	}
	
	@Test
	public void OverrideTicketTest() {
		// server should accept overrides of active tickets
		sendMessage("ot:TI25:49.99");
		Message response = getMessage();
		assertEquals("ot:overridden_ticket", response.getText());

		// server should return "ticket not found" message
		sendMessage("ot:TI999:5.00");
		response = getMessage();
		assertEquals("ot:ticket_not_found", response.getText());

		// server should reject invalid fee values
		sendMessage("ot:TI25:-35");
		response = getMessage();
		assertEquals("ot:invalid_fee", response.getText());

		// server should reject if a ticket has already been paid for
		sendMessage("ot:TI2:300.00");
		response = getMessage();
		assertEquals("ot:already_paid", response.getText());
	}
	
	@Test
	public void ViewReportTest() {
		// server should return human-readable report of garage
		sendMessage("vr");
		Message response = getMessage();
		assertTrue(response.getText().startsWith("vr:"));
	}
	
	@Test
	public void ModifyRateTest() {
		// server should accept integers
		sendMessage("mr:100");
		Message response = getMessage();
		assertEquals("mr:modified_rate", response.getText());

		// server should accept floating point numbers
		sendMessage("mr:79.99");
		response = getMessage();
		assertEquals("mr:modified_rate", response.getText());

		// server should reject negative values
		sendMessage("mr:-50.01");
		response = getMessage();
		assertEquals("mr:invalid_rate", response.getText());
	}
	
	@Test
	public void ViewActiveTicketsTest() {
		// server should return all active ticket ids in garage
		sendMessage("vv");
		Message response = getMessage();
		String split[] = response.getText().substring("vv:".length()).split(",");
		assertEquals(15 + 1, split.length); // should be 15 active tickets + 1 comma
	}
	
	@Test
	public void ViewCameraIDsTest() {
		// server should return the ids of all cameras in garage
		sendMessage("vc");
		Message response = getMessage();
		assertTrue(response.getText().startsWith("vc:") && response.getText().contains(","));

		// server should not return any ids of cameras from other garages
		sendMessage("vc");
		response = getMessage();
		assertFalse(response.getText().contains("SC1"));
	}

	@Test
	public void ViewCameraFeedTest() {
		// server should return "camera not found" message if camera has not been found
		sendMessage("vf:SC99");
		ImageMessage response = getImageMessage();
		assertEquals("vf:camera_not_found", response.getText());

		// server should return message containing the live camera feed
		sendMessage("vf:SC0");
		response = getImageMessage();
		assertEquals("vf:image", response.getText());
		assertFalse(response.getImage() == null); // actual image should be returned by server
	}
	
	@Test
	public void ViewGarageLogsTest() {
		// server should return all logs associated with garage in a single human-readable string
		sendMessage("vl");
		Message response = getMessage();
		assertTrue(response.getText().startsWith("vl:") && response.getText().contains("\n")); // log entries are seperated by newlines
	}
	
	@Test
	@Order(Integer.MAX_VALUE) // last test
	public void LogoutTest() {
		// server should acknowledge client logout
		sendMessage("lo");
		Message response = getMessage();
		assertEquals("lo:signed_out", response.getText());
	}
}
