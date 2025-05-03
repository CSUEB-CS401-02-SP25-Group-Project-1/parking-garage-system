package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import shared.*;

public class EmployeeGUI {
    private static final int PORT = 7777;
    private static String serverIP; // ip is preserved for reconnections
    private static String garageID; // id is preserved for reconnections
    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void main(String[] args) {
        //while (true) { // loop for logouts
            initScreen();
            loginScreen();
            dashboardScreen();
        //}
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
                garageID = JOptionPane.showInputDialog("Enter garage ID:");
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
        // window config
        JFrame window = new JFrame("Employee Login");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO: find out how to call exit() method
		window.setSize(560, 480);
		window.setLocationRelativeTo(null); // center on screen
		window.setVisible(true); // make visible

        // panel layouts
        //LayoutManager boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        LayoutManager flowLayout = new FlowLayout();

        // panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(flowLayout);

        JPanel usernamePanel = new JPanel();
        mainPanel.setLayout(flowLayout);

        JPanel passwordPanel = new JPanel();
        mainPanel.setLayout(flowLayout);

        // widgets
        
        // notice text
        JLabel noticeLabel = new JLabel("Welcome! Please login below.");

        // username
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(10);

        // password
        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField(10);

        // submit button
        JButton submitButton = new JButton("Submit");

        // widget action listeners
        submitButton.addActionListener(new ActionListener() { // search button action listener
			public void actionPerformed(ActionEvent e) {
				if (submitCredentials(usernameField.getText(), passwordField.getText())) { // if credentials were valid
                    // proceed to next screen
                    window.dispose();
                    return;
                } else { // otherwise, show error message
                    JOptionPane.showMessageDialog(window, "Invalid credentials. Please try again.", 
                                                  "ERROR", JOptionPane.ERROR_MESSAGE);
                    usernameField.setText(""); // clear fields
                    passwordField.setText("");
                }
			}
	    });

        // add widgets to panels
        mainPanel.add(noticeLabel);
        mainPanel.add(usernamePanel);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        mainPanel.add(passwordPanel);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        mainPanel.add(submitButton);

        // make the submit button the default selection
        window.getRootPane().setDefaultButton(submitButton);

        // add panels to window
        window.add(mainPanel);
    }

    private static void dashboardScreen() {
        // TODO: display current username
        // TODO: display garage name (and id)
        // TODO: display current parking availability
        // TODO: display list of active tickets in garage
        // TODO: display list of cameras
        // TODO: display gate status
        // TODO: employee action buttons
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

    private static boolean connectToServer(String ip) {
        try {
            socket = new Socket(ip, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) { // if connection failed
            serverIP = null; // clear current server IP in memory
            return false;
        }
        return true;
    }

    private static boolean requestGarage(String id) {
        id = id.toUpperCase();
        sendMessage("init:"+id+":em");
        Message response = getMessage();
        if (response.getText().equals("init:assigned_em")) {
            return true;
        }
        // if server returned error response
        garageID = null; // clear current garage ID in memory
        return false;
    }

    private static boolean submitCredentials(String username, String password) {
        sendMessage("li:"+username+":"+password);
        Message response = getMessage();
        if (response.getText().equals("li:successful")) {
            return true;
        }
        return false;
    }
}
