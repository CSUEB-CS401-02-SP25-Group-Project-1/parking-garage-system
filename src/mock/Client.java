package mock;

import shared.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

public class Client { // mock client, do not use in actual production code!
	final static int PORT = 7777; // should be same as server's
	public static void main(String args[]) {
		try {
			Scanner input = new Scanner(System.in);
			System.out.print("Enter server IP: ");
			String serverIP = input.nextLine();
			Socket socket = new Socket(serverIP, PORT); // connects to server
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			String text;
			do {
				text = input.nextLine();
				Message msg = new Message(MessageType.Request, "mockClient", text);
				out.writeObject(msg);
				Message response = (Message)in.readObject();
				System.out.println(response.getType()+": "+response.getText());
			} while (!text.equalsIgnoreCase("exit"));
			input.close();
			socket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
