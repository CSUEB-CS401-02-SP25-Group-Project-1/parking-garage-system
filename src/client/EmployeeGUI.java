package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import shared.*;

public class EmployeeGUI {
    private static final int PORT = 7777;
    private static String serverIP; // ip is preserved for reconnections
    private static String garageID; // id is preserved for reconnections
    private static String user; // username of currently logged-in employee (resets after logout)
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
        JPasswordField passwordField = new JPasswordField(10);

        // submit button
        JButton submitButton = new JButton("Submit");

        // widget action listeners
        submitButton.addActionListener(new ActionListener() { // search button action listener
			public void actionPerformed(ActionEvent e) {
				if (submitCredentials(usernameField.getText(), passwordField.getPassword().toString())) { // if credentials were valid
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

        // window config
        JFrame window = new JFrame("Employee Dashboard");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO: find out how to call exit() method
		window.setSize(560, 480);
		window.setLocationRelativeTo(null); // center on screen
		window.setVisible(true); // make visible

        // TODO: add panels

        // widgets
        JLabel usernameLabel = new JLabel("Logged in as: "+user);
        JLabel garageLabel = new JLabel("Garage: "+getGarageName()+" ("+garageID+")");
        JLabel availabilityLabel = new JLabel(); // gets updated
        JLabel gateStatusLabel = new JLabel(); // gets updated
        JLabel gateTimeLabel = new JLabel(); // gets updated
        JLabel rateLabel = new JLabel(); // gets updated
        JList<String> activeTicketsList = new JList<>(); // gets updated
        JList<String> camerasList = new JList<>(); // gets updated

        // employee action buttons
        JButton generateTicketButton = new JButton("Generate Ticket");
        JButton payTicketButton = new JButton("Pay Ticket"); // billing summary is also viewed through here
        JButton toggleGateButton = new JButton("Toggle Gate");
        JButton changePasswordButton = new JButton("Change Password");
        JButton changeGateTimeButton = new JButton("Change Gate Opening Time");
        JButton changeRateButton = new JButton("Change Hourly Rate");
        JButton overrideTicketButton = new JButton("Override Ticket");
        JButton viewReportButton = new JButton("View Garage Report");
        JButton viewLogsButton = new JButton("View Garage Logs");
        JButton logoutButton = new JButton("Logout");

        // start dashboard updater
        DashboardUpdater updater = new DashboardUpdater(availabilityLabel, gateStatusLabel,
        gateTimeLabel, rateLabel, activeTicketsList, camerasList);
        new Thread(updater).start();
    }

    // more helper methods

    private static String getGarageName() {
        sendMessage("gn");
        return getMessage().getText();
    }

    private static void exit() { // closes all connections before exiting // TODO: revise
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
            user = username;
            return true;
        }
        return false;
    }

    private static class DashboardUpdater implements Runnable {
        private int POLL_TIME = 10; // how many seconds until updater sends requests again
        private boolean isRunning = true; // flag for updater to continue running until calling "stop()"
        // associated widgets
        JLabel availabilityLabel;
        JLabel gateStatusLabel;
        JLabel gateTimeLabel;
        JLabel rateLabel;
        JList<String> activeTicketsList;
        JList<String> camerasList;

        public DashboardUpdater(JLabel availabilityLabel, JLabel gateStatusLabel, 
        JLabel gateTimeLabel, JLabel rateLabel, JList<String> activeTicketsList,
        JList<String> camerasList) {
            this.availabilityLabel = availabilityLabel;
            this.gateStatusLabel = gateStatusLabel;
            this.gateTimeLabel = gateTimeLabel;
            this.rateLabel = rateLabel;
            this.activeTicketsList = activeTicketsList;
            this.camerasList = camerasList;
        }

        public void run() {
            while (isRunning) {
                update();
                try {
                    Thread.sleep(POLL_TIME * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            isRunning = false;
        }

        public synchronized void update() { // synchronized to prevent other threads from updating at the same time
            updateAvailability();
            updateActiveTicketsList();
            updateCamerasList();
            updateGateStatus();
            updateRate();
            updateGateTime();
        }

        // methods to update individual widgets

        private void updateAvailability() {
            availabilityLabel.setText("Available spaces: "+getAvailability());
        }

        private void updateActiveTicketsList() {
            ArrayList<String> activeTicketIDs = getActiveTicketIDs();
            activeTicketsList = new JList<>(getListModel(activeTicketIDs));
        }

        private void updateCamerasList() {
            ArrayList<String> cameraIDs = getCameraIDs();
            activeTicketsList = new JList<>(getListModel(cameraIDs));
        }

        private void updateGateStatus() {
            if (isGateOpen()) {
                gateStatusLabel.setText("GATE OPEN");
                gateStatusLabel.setForeground(Color.GREEN);
            } else {
                gateStatusLabel.setText("GATE CLOSED");
                gateStatusLabel.setForeground(Color.RED);
            }
        }

        private void updateRate() {
            rateLabel.setText("Hourly rate: $"+getRate());
        }

        private void updateGateTime() {
            gateTimeLabel.setText("Gate open time: "+getGateOpenTime());
        }

        // more helper methods

        private int getAvailability() {
            sendMessage("va");
            Message response = getMessage();
            return Integer.parseInt(response.getText().substring("va:".length()));
        }

        private String geAllIDsAsString(String command) {
            sendMessage(command);
            Message response = getMessage();
            return response.getText().substring((command+":").length());
        }

        private ArrayList<String> getIDsFromString(String string) {
            String split[] = string.split(",");
            ArrayList<String> ids = new ArrayList<>();
            for (String id : split) {
                if (!id.isEmpty()) {
                    ids.add(id);
                }
            }
            return ids;
        }

        private ArrayList<String> getActiveTicketIDs() {
            return getIDsFromString(geAllIDsAsString("vv"));
        }

        private ArrayList<String> getCameraIDs() {
            return getIDsFromString(geAllIDsAsString("vc"));
        }

        private DefaultListModel<String> getListModel(ArrayList<String> strings) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (String str : strings) {
                listModel.addElement(str);
            }
            return listModel;
        }

        private boolean isGateOpen() {
            sendMessage("gs");
            Message response = getMessage();
            if (response.getText().equals("gs:gate_open")) {
                return true;
            }
            return false;
        }

        private String getRate() {
            sendMessage("gr"); // TODO: to implement
            Message response = getMessage();
            double rawRate = Double.parseDouble(response.getText().substring("gr:".length()));
            return String.format("%.2f", rawRate);
        }

        private double getGateOpenTime() {
            sendMessage("go"); // TODO: to implement
            Message response = getMessage();
            return Double.parseDouble(response.getText().substring("go:".length()));
        }
    }
}
