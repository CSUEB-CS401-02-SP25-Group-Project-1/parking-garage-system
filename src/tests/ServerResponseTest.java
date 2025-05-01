package tests;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Paths;
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
