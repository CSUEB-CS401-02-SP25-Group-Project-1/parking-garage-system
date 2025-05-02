package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import shared.*;

public class EmployeeGUI {
    private static final int PORT = 7777;
    private static String serverIP; // ip is preserved for reconnections
    private static String garageID; // id is preserved for reconnections
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void main(String[] args) {
        while (true) { // loop for logouts
            initScreen();
            loginScreen();
            dashboardScreen();
        }
    }

    // main windows

    private static void initScreen() {
        while (true) {
            if (serverIP == null) {
                serverIP = JOptionPane.showInputDialog("Enter server IP:");
                if (serverIP == null) { // if user didn't specify server IP
                    exit(); // close program
                }
            }
            if (garageID == null) {
                String garageID = JOptionPane.showInputDialog("Enter garage ID:");
                if (garageID == null) { // if user didn't specify garage ID
                    exit(); // close program
                }
            }
            if (!connectToServer(serverIP)) { // attempt to connect to server
                JOptionPane.showMessageDialog(null, "Unable to connect to server at this time", 
                                             "ERROR", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!requestGarage(garageID)) { // attempt to request garage from server
                JOptionPane.showMessageDialog(null, "Garage not found", 
                                             "ERROR", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            return;
        }
    }

    private static void loginScreen() {
        JFrame frame = new JFrame("Employee Login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO: find out how to call exit() method
		frame.setSize(560, 480);
		frame.setLocationRelativeTo(null); // center on screen
		frame.setVisible(true);	// make visible
		// UI stuff

    }

    private static void dashboardScreen() {

    }

    // more helper methods
    private static void exit() { // closes all connections before exiting
        if (socket != null) {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    private static void sendMessage(String text) {
        Message message = new Message(MessageType.Request, "employeeClient", text);
        try {
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Message getMessage() {
        Message message = null;
        try {
            message = (Message)in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private static boolean connectToServer(String serverIP) {
        try {
            socket = new Socket(serverIP, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) { // if connection failed
            serverIP = null; // clear current server IP in memory
            return false;
        }
        return true;
    }

    private static boolean requestGarage(String garageID) {
        garageID = garageID.toUpperCase();
        sendMessage("init:"+garageID+":em");
        Message response = getMessage();
        if (response.getText().equals("init:assigned_em")) {
            return true;
        }
        // if server returned error response
        garageID = null; // clear current garage ID in memory
        return false;
    }
}
