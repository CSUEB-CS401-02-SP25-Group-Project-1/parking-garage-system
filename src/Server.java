package server;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class Server{

	private Employee[] employees;	// list of all employees for login
	private Garage[] garages;	// list of garages for IP authentication
	private String serverIP = InetAddress.getLocalHost(); //wrong method?

	public static void main(String[] args) {
		// Create server socket
		// Listen for connection
		// Throw connection onto new Thread()

		// which port to use?
		ServerSocket ss = new ServerSocket(59080);

		// have better condition?
		while (true) {
			Socket socket = listener.accept();
			new Thread(new ClientHandler(socket));
		}
	}

	private class ClientHandler {
		private Socket socket;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private long threadID = Thread.currentThread.getId();
		private boolean isConnected;
		private boolean isRunning;

		private List<Message> outboundMessages;
		private List<Message> inboundMessages;

		private User client; // can we make this Customer or Employee?
		private Garage garage;

		public ClientHandler(Socket socket) {
			this.socket = socket;
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			isConnected = false; // user has not completed init();
			isRunning = true;
			
			// some default value
			client = null;
			garage = null;
		}

		public void run() {
			// make init a requirement for the client to send?
			while (isRunning) {
				try {
					inboundMessages = in.readObject();
					inboundMessages.forEach(msg -> processMessages(msg));
				} catch (IOException | ClassNotFoundException e) {
					System.err.println(e); continue;
				}

				// processMessages prepares outboundMessages autonomously
				out.writeObject(outboundMessages);
			}
		}
		
		private void processMessage(Message message) {
			String from = message.getSender();
			MessageType type = message.getType();

			String[] contents = message.getText().split(":");

			String command = contents[0];
			String data = contents[1];
			
			// assume that messageType will be Request?
			switch (command) {
			case "in":
				init(from, data); 
				// `data` holds IP/GUI_ID
				// `from` distiguishes customer/employee

			case "gt": // generate ticket
				generateTicket();
				// return ticket ID in Message if successful

			case "pt": // pay ticket
				payTicket(data); // `data` holds amount paid

			case "vr": // view report (unique for emp/cust)
				viewReport(data);
				// Report is stuffed into Message
				// Customers see # cars in garage
				// Employees get report
					// length according to `data`

			case "tg": // toggle gate (emp)
				toggleGate(); // check for employee
				break;

			case "li": // log in (emp?)
				login(data) // data holds username,password
				// do customer GUIs need a login?
				break;

			case "mp": // modify password (emp)
				modifyPassword(data)
				// `data` hold oldpassword,newpassword
				break;
	
			case "ot": // override ticket (emp)
				overrideTicket(data); // `data` holds id
				break;

			case "mr": // modify rate (emp)
				modifyRate(data); // data holds new rate
				break;
			
			case "vl": // view log (emp)
				viewLog(data);
					// `data` hold earliest,latest
				break;

			case "vv": // view vehicles (list of ids)(emp)
				viewVehicles();
				// check for employee login
				// return list of IDs from garage
				break;

			case "vc": // view cameras (emp)
				viewCameras();
				//return fake image?
				break;

			case "va": // view amount of cars (emp)
				viewAmountCars();
				break;

			default: // invalid command
				outboundMessages.add(
				new Message(MessageType.FAIL, 
					serverIP, "Unrecognized Command"));
				break;
			}
		}

		private void init(String clientIP, String ID) {
			// Find client status
			// Connect IP from user to their garage
			
			String clientIP;

			// Send IP request?

			// some way to make handle for user
			switch(ID.charAt(0)) {
				case 'C': User = new Customer(garage); break;
				case 'E': User = new Employee(garage); break;
				default: // bad entry
				outboundMessages.add(
					new Message(MessageType.FAIL,
					serverIP, "Unrecognized usertype");
				return; // abort
			}

			// find corresponding ip/id from garages
			Garage garage;
			for (int i = 0; i < garages.length(); i++) {
				String[] ips = garages[i].getIPs();
				for (int j = 0; j < ips.length(); j++) {
					if (ips[j].compareTo(clientIP) == 0) {
						this.garage = garages[i];
					}
				}
			}

			if (garage == null) { // no matching IPs found
				outboundMessages.add(new Message(
					MessageType.FAIL, serverIP,
					"Unrecognized IP."));
				return; //abort
			}

			// if everything succeeds, make a success message
			outboundMessages.add(new Message(
				MessageType.SUCCESS, serverIP, 
				"Initialized connection."));

			return;
		}
	}	
}
