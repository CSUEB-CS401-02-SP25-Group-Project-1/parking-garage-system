package mock;

import shared.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client { // mock client, do not use in actual production code!
	final static int PORT = 7777; // should be same as server's
	public static void main(String args[]) {
		try {
			Scanner input = new Scanner(System.in);
			System.out.print("Enter server IP: ");
			String serverIP = input.nextLine();
			input.close();
			Socket socket = new Socket(serverIP, PORT); // connects to server
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
