package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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
        initScreen(); // will open each screen by chaining
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
            System.out.println(serverIP+" "+garageID);
            loginScreen();
            return;
        }
    }

    private static void loginScreen() {
        JFrame window = new JFrame("Employee Login");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(560, 480);
        window.setLocationRelativeTo(null);
        window.setLayout(new GridBagLayout()); // main layout
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(10, 10, 10, 10); // padding
    
        // notice label
        JLabel noticeLabel = new JLabel("Welcome! Please login below.");
        noticeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridwidth = 2;
        grid.anchor = GridBagConstraints.CENTER;
        window.add(noticeLabel, grid);
    
        // username
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        grid.gridwidth = 1;
        grid.gridy = 1;
        grid.gridx = 0;
        grid.anchor = GridBagConstraints.EAST;
        window.add(usernameLabel, grid);
        grid.gridx = 1;
        grid.anchor = GridBagConstraints.WEST;
        window.add(usernameField, grid);
    
        // password
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        grid.gridy = 2;
        grid.gridx = 0;
        grid.anchor = GridBagConstraints.EAST;
        window.add(passwordLabel, grid);
        grid.gridx = 1;
        grid.anchor = GridBagConstraints.WEST;
        window.add(passwordField, grid);
    
        // submit button to right of password field
        JButton submitButton = new JButton("Submit");
        grid.gridy = 2;
        grid.gridx = 2;
        grid.anchor = GridBagConstraints.WEST;
        window.add(submitButton, grid);
    
        // submit button action
        submitButton.addActionListener(new ActionListener() { // add button action listener
			public void actionPerformed(ActionEvent e) {
                int returnCode = submitCredentials(usernameField.getText(), new String(passwordField.getPassword()));
                switch (returnCode) {
                    case 1: // success
                    // proceed to next screen
                    dashboardScreen(); 
                    window.dispose(); // close current screen
                    break;
                    case 0: // invalid credentials
                    JOptionPane.showMessageDialog(window, "Invalid credentials. Please try again.",
                                                  "ERROR", JOptionPane.ERROR_MESSAGE);
                    usernameField.setText(""); // clear fields
                    passwordField.setText("");
                    break;
                    case -1: // unable to reach server
                    JOptionPane.showMessageDialog(window, "Unable to reach server at this time",
                                                  "ERROR", JOptionPane.ERROR_MESSAGE);
                }
			}
	    });

        // make the submit button the default button
        window.getRootPane().setDefaultButton(submitButton); 

        // draw window
        window.setVisible(true);
    }

    private static void dashboardScreen() {
        // window config
        JFrame window = new JFrame("Employee Dashboard");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(560, 600);
		window.setLocationRelativeTo(null); // center on screen
        window.setLayout(new BorderLayout());

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

        // panel layout

        // tickets panel (left side)
        JPanel ticketsPanel = new JPanel(new BorderLayout());
        ticketsPanel.setBorder(BorderFactory.createTitledBorder("Active Tickets"));
        ticketsPanel.add(new JScrollPane(activeTicketsList), BorderLayout.CENTER);

        // label panel (top right)
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        Font boldFont = new Font("SansSerif", Font.BOLD, 14);
        usernameLabel.setFont(boldFont);
        garageLabel.setFont(boldFont);
        rateLabel.setFont(boldFont);
        availabilityLabel.setFont(boldFont);
        gateTimeLabel.setFont(boldFont);
        gateStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 24)); // even bolder!

        labelPanel.add(usernameLabel);
        labelPanel.add(garageLabel);
        labelPanel.add(rateLabel);
        labelPanel.add(availabilityLabel);
        labelPanel.add(gateTimeLabel);
        labelPanel.add(gateStatusLabel);

        // cameras panel (bottom right)
        JPanel camerasPanel = new JPanel(new BorderLayout());
        camerasPanel.setBorder(BorderFactory.createTitledBorder("Cameras"));
        camerasPanel.add(new JScrollPane(camerasList), BorderLayout.CENTER);

        // right panel (stacks label and cameras panel)
        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        rightPanel.add(labelPanel);
        rightPanel.add(camerasPanel);

        // main panel (center)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(ticketsPanel);
        mainPanel.add(rightPanel);
        window.add(mainPanel, BorderLayout.CENTER);

        // button panel (bottom area)
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // wraps buttons (2 per row)
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(generateTicketButton);
        buttonPanel.add(payTicketButton);
        buttonPanel.add(toggleGateButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(changeGateTimeButton);
        buttonPanel.add(changeRateButton);
        buttonPanel.add(overrideTicketButton);
        buttonPanel.add(viewReportButton);
        buttonPanel.add(viewLogsButton);
        buttonPanel.add(logoutButton);
        window.add(buttonPanel, BorderLayout.SOUTH);

        // start dashboard updater
        DashboardUpdater updater = new DashboardUpdater(availabilityLabel, gateStatusLabel,
        gateTimeLabel, rateLabel, activeTicketsList, camerasList);
        new Thread(updater).start();

        // "closing window" procedure
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout(window, updater);
                System.exit(0); // close program
            }
        });

        // list selection listener for active tickets list
        activeTicketsList.addListSelectionListener(new ListSelectionListener() {
	    	public void valueChanged(ListSelectionEvent e) {
	    		if (!e.getValueIsAdjusting()) { // detects whenever a list element is clicked once
	    			String selectedTicketID = activeTicketsList.getSelectedValue();
                    if (selectedTicketID != null) {
                        payTicket(window, updater, selectedTicketID);
                    }
	    		}
	    	}
	    });

        // list selection listener for cameras list
        camerasList.addListSelectionListener(new ListSelectionListener() {
	    	public void valueChanged(ListSelectionEvent e) {
	    		if (!e.getValueIsAdjusting()) { // detects whenever a list element is clicked once
	    			String selectedCameraID = camerasList.getSelectedValue();
                    if (selectedCameraID != null) {
                        viewCameraFeed(window, selectedCameraID);
                    }
	    		}
	    	}
	    });

        // action listeners for buttons
        generateTicketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateTicket(window, updater);
			}
	    });

        payTicketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				payTicket(window, updater, null); // null because no ticket has been selected
			}
	    });

        toggleGateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleGate(window, updater);
			}
	    });

        changePasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword(window);
			}
	    });

        changeGateTimeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeGateTime(window, updater);
			}
	    });

        changeRateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeRate(window, updater);
			}
	    });

        overrideTicketButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				overrideTicket(window);
			}
	    });

        viewReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewReport(window);
			}
	    });

        logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logout(window, updater);
			}
	    });

        viewLogsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewLogs(window);
			}
	    });

        // draw window
        window.setVisible(true);
    }

    // command methods

    private static void viewCameraFeed(JFrame window, String cameraID) {
        sendMessage("vf:"+cameraID);
        ImageMessage response = getImageMessage();
        if (response.getText().equals("vf:camera_not_found")) {
            JOptionPane.showMessageDialog(window, "Unable to get live camera feed: camera not found", 
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (response.getText().equals("vf:feed_unavailable")) {
            JOptionPane.showMessageDialog(window, "Unable to get live camera feed: feed unavailable", 
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!response.getText().equals("vf:image")) {
            JOptionPane.showMessageDialog(window, "Unable to get live camera feed at this time", 
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ImageIcon liveFeed = response.getImage();
        // display live feed on a new pop-up window
        JDialog dialog = new JDialog(window, "Camera Feed", true); // true = modal
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel(liveFeed); // labels can also be images
        dialog.add(label);
        dialog.pack(); // size to fit image
        dialog.setLocationRelativeTo(window); // center on parent
        dialog.setVisible(true); // shows and blocks until closed
    }

    private static void generateTicket(JFrame window, DashboardUpdater updater) {
        sendMessage("gt");
        Message response = getMessage();
        if (response.getText().equals("gt:unavailable_space")) {
            JOptionPane.showMessageDialog(window, "Unable to generate ticket: no more space in garage", 
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String ticketID = response.getText().substring("gt:".length());
        JOptionPane.showMessageDialog(window, "Generated ticket: "+ticketID,
                                      "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        updater.update(); // update ui to reflect changes
    }

    private static void payTicket(JFrame window, DashboardUpdater updater, String ticketID) {
        if (ticketID == null) {
            ticketID = JOptionPane.showInputDialog(window, "Enter ticket ID:");
            if (ticketID == null) { // user cancellation
                return;
            }
            ticketID = ticketID.toUpperCase();
        }
        // payment confirmation
        Double paymentAmount = displayBillingSummary(window, ticketID); // returns fee after payment confirmation
        if (paymentAmount == null) { // if user didn't pay or invalid ticket id
            return; // cancel payment
        }
        // process payment
        sendMessage("pt:"+ticketID+":"+paymentAmount);
        Message response = getMessage();
        if (response.getText().equals("pt:ticket_not_found")) {
            JOptionPane.showMessageDialog(window, "Ticket not found: "+ticketID,
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (response.getText().equals("pt:already_paid")) {
            JOptionPane.showMessageDialog(window, "Ticket has already been paid: "+ticketID,
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (response.getText().equals("pt:incorrect_amount")) {
            JOptionPane.showMessageDialog(window, "Incorrect amount has been paid for "+ticketID,
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!response.getText().startsWith("pt:")) { // if server didn't return "pay ticket" response at all
            JOptionPane.showMessageDialog(window, "An unknown error has occurred",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // view receipt
        String receipt = response.getText().substring("pt:".length());
        viewReceipt(window, receipt); // view receipt in another pop-up dialog
        // update ui to reflect changes
        updater.update(); 
    }

    private static void toggleGate(JFrame window, DashboardUpdater updater) {
        sendMessage("tg");
        Message response = getMessage();
        if (!response.getText().equals("tg:toggled")) {
            JOptionPane.showMessageDialog(window, "Garage gate cannot be toggled at this time",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        updater.update(); // update ui to reflect changes
    }

    private static void changePassword(JFrame window) { // no need to update widgets
        // workaround for swing having no input dialogue for obscuring passwords like jpasswordfield
        JPasswordField passwordField = new JPasswordField();
        Object[] prompt = {"Enter new password:", passwordField};
        int option = JOptionPane.showConfirmDialog(window, prompt, "Change password",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != 0) { // user cancellation
            return; 
        }
        // send new password to server
        String newPassword = new String(passwordField.getPassword());
        sendMessage("mp:"+newPassword);
        Message response = getMessage();
        if (response.getText().equals("mp:no_special_characters")) {
            JOptionPane.showMessageDialog(window, "Password must contain special characters",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!response.getText().equals("mp:password_changed")) {
            JOptionPane.showMessageDialog(window, "An unknown error has occurred",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(window, "Password has been successfully changed",
                                      "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void changeGateTime(JFrame window, DashboardUpdater updater) {
        Double newGateTime = getValidDouble(window, "gate open time");
        if (newGateTime == null) { // user cancellation
            return;
        }
        sendMessage("mg:"+newGateTime);
        Message response = getMessage();
        if (!response.getText().equals("mg:time_updated")) {
            JOptionPane.showMessageDialog(window, "Unable to change gate open time at this time",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(window, "Gate open time has been successfully changed",
                                      "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        updater.update(); // update ui to reflect changes
    }

    private static void changeRate(JFrame window, DashboardUpdater updater) {
        Double newRate = getValidDouble(window, "hourly rate");
        if (newRate == null) { // user cancellation
            return;
        }
        sendMessage("mr:"+newRate);
        Message response = getMessage();
        if (!response.getText().equals("mr:modified_rate")) {
            JOptionPane.showMessageDialog(window, "Unable to change hourly rate at this time",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(window, "Hourly rate has been successfully changed",
                                      "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        updater.update(); // update ui to reflect changes
    }

    private static void overrideTicket(JFrame window) { // no need to update widgets
        String ticketID = JOptionPane.showInputDialog(window, "Enter ticket ID:");
        if (ticketID == null) { // user cancellation
            return;
        }
        ticketID = ticketID.toUpperCase();
        Double newFee = getValidDouble(window, "ticket fee");
        if (newFee == null) { // user cancellation
            return;
        }
        sendMessage("ot:"+ticketID+":"+newFee);
        Message response = getMessage();
        if (response.getText().equals("ot:ticket_not_found")) {
            JOptionPane.showMessageDialog(window, "Ticket not found",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (response.getText().equals("ot:already_paid")) {
            JOptionPane.showMessageDialog(window, "Ticket has already been paid",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (response.getText().equals("ot:invalid_fee")) {
            JOptionPane.showMessageDialog(window, "Invalid fee",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!response.getText().equals("ot:overridden_ticket")) {
            JOptionPane.showMessageDialog(window, "Unable to override ticket at this time",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(window, "Ticket fee has been successfully overridden",
                                      "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void viewReport(JFrame window) {
        sendMessage("vr");
        Message response = getMessage();
        if (!response.getText().startsWith("vr:")) {
            JOptionPane.showMessageDialog(window, "Unable to view report at this time",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String report = assembleReport(response.getText().substring("vr:".length()));
        if (report == null) {
            JOptionPane.showMessageDialog(window, "Error while processing report",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(window, report, "Garage Report",
                                      JOptionPane.PLAIN_MESSAGE);
    }

    private static void viewLogs(JFrame window) {
        sendMessage("vl");
        Message response = getMessage();
        if (!response.getText().startsWith("vl:")) {
            JOptionPane.showMessageDialog(window, "Unable to get server logs at this time",
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String allLogsString = response.getText().substring("vl:".length());
        // setup text area widget
        JTextArea textArea = new JTextArea(allLogsString);
        textArea.setEditable(false); // immutable
        textArea.setLineWrap(true); // wrap long lines
        textArea.setWrapStyleWord(true); // wrap at word boundaries
        // wrap it in a scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // size of popup
        // show it as a dialog rather than a new window
        JOptionPane.showMessageDialog(window, scrollPane, "Garage Logs",
                                      JOptionPane.PLAIN_MESSAGE);
    }

    private static void logout(JFrame window, DashboardUpdater updater) {
        sendMessage("lo");
        Message response = getMessage();
        if (!response.getText().equals("lo:signed_out")) {
            JOptionPane.showMessageDialog(window,
            "Unable to sign employee out of server. Closing dashboard anyway.",
            "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(window, "Signed out of server",
                                      "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        updater.stop(); // stop updater
        closeConnection(); // close connection
        user = null; // reset user
        initScreen(); // go through init process again
        window.dispose(); // close dashboard
    }

    // more helper methods

    private static String assembleReport(String reportData) { // assembles a human-readable report from server response data
        System.out.println(reportData);
    	String split[] = reportData.split(",");
        if (split.length != 9) { // server should have returned 9 parameters (one more)
            return null;
        }
        String revenueThisHour = formatAmountString(split[0]);
        String revenueToday = formatAmountString(split[1]);
        String revenueThisWeek = formatAmountString(split[2]);
        String revenueThisMonth = formatAmountString(split[3]);
        String revenueThisYear = formatAmountString(split[4]);
        String revenueAllTime = formatAmountString(split[5]);
        
        int peakHour = Integer.parseInt(split[6]);
        String peakHourStr = "";
        // if == 0
        // if == 12
        // else if < 12
        if (peakHour == 0) {
        	peakHourStr = "12 am";
        } else if (peakHour == 12) {
        	peakHourStr = "12 pm";
        } else if (peakHour < 12) {
        	peakHourStr += peakHour + " am";
        } else {
        	peakHourStr += (peakHour-12) + " pm";
        }
        String totalParkedEver = split[7];
        String currentlyParkedVehicles = split[8];
        
        String report = "Revenue generated this hour: "+revenueThisHour+"\n"+
                        "Revenue generated today: "+revenueToday+"\n"+
                        "Revenue generated this week: "+revenueThisWeek+"\n"+
                        "Revenue generated this month: "+revenueThisMonth+"\n"+
                        "Revenue generated this year: "+revenueThisYear+"\n"+
                        "All-time revenue generated: "+revenueAllTime+"\n"+
                        "Peak parking hour: "+peakHourStr+"\n"+
                        "Total cars entered: "+totalParkedEver+"\n"+
                        "Currently parked vehicles: "+currentlyParkedVehicles;
        return report;
    }

    private static String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private static Double getValidDouble(JFrame window, String what) { // Double to return null on cancellation
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(window, "Enter new "+what+":");
                if (input == null) { // user cancellation
                    return null;
                }
                double inputTest = Double.parseDouble(input);
                if (inputTest < 0) {
                    JOptionPane.showMessageDialog(window, capitalize(what)+" must be a positive value",
                                                  "ERROR", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                return inputTest;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(window, capitalize(what)+" must be a number",
                                              "ERROR", JOptionPane.ERROR_MESSAGE);
                continue;
            }
        }
    }

    private static void viewReceipt(JFrame window, String receipt) {
        JOptionPane.showMessageDialog(window, receipt, "Receipt",
                                      JOptionPane.PLAIN_MESSAGE);
    }

    private static String formatAmountString(String amountStr) {
        double rawValue = Double.parseDouble(amountStr);
        return "$"+String.format("%.2f", rawValue);
    }

    private static Double displayBillingSummary(JFrame window, String ticketID) {
        sendMessage("bs:"+ticketID);
        Message response = getMessage();
        if (response.getText().equals("bs:invalid_ticket_id")) {
            JOptionPane.showMessageDialog(window, "Invalid ticket: "+ticketID,
                                          "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        String billing = response.getText().substring("bs:".length());
        String split[] = billing.split(":");
        double paymentAmount = Double.parseDouble(split[3]);
        
        String prompt;
        //If null for garage exit,
        //putting "STILL ACTIVE" for the exit time
        if(split[2].equals("STILL ACTIVE")) {
        	prompt = "Billing summary for ticket:\n"+
                    "Ticket ID: "+split[0]+"\n"+
                    "Entry time: "+new Date(Long.parseLong(split[1]))+"\n"+
                    "Exit time: "+ "STILL ACTIVE"+"\n"+
                    "Total due: "+formatAmountString(split[3]);
        }
        else {
	        prompt = "Billing summary for ticket:\n"+
	                        "Ticket ID: "+split[0]+"\n"+
	                        "Entry time: "+new Date(Long.parseLong(split[1]))+"\n"+
	                        "Exit time: "+new Date(Long.parseLong(split[2]))+"\n"+
	                        "Total due: "+formatAmountString(split[3]);
	    }
        Object[] options = {"Pay", "Cancel"}; // custom button names for payment prompt
        int paymentPrompt = JOptionPane.showOptionDialog(
            window,
            prompt,                             // message
            "Payment Confirmation",             // title
            JOptionPane.YES_NO_OPTION,          // option type
            JOptionPane.QUESTION_MESSAGE,       // message type
            null,                               // icon (null = default)
            options,                            // custom button text array
            options[0]                          // default selected button
        );
        if (paymentPrompt != 0) { // if user chose not to pay
            return null;
        }
        return paymentAmount; // return actual payment amount
    }

    private static String getGarageName() {
        sendMessage("gn");
        Message response = getMessage();
        String garageName = response.getText().substring("gn:".length());
        return garageName;
    }

    private static void closeConnection() {
        if (socket != null) {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void exit() {
        closeConnection(); // closes all connections before exiting 
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

    private static ImageMessage getImageMessage() {
        ImageMessage message = null;
        try {
            message = (ImageMessage)in.readObject();
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

    private static int submitCredentials(String username, String password) {
        sendMessage("li:"+username+":"+password);
        Message response = getMessage();
        if (response == null) {
            return -1;
        }
        if (response.getText().equals("li:successful")) {
            user = username;
            return 1;
        }
        return 0;
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
            activeTicketsList.setModel(getListModel(activeTicketIDs));
        }

        private void updateCamerasList() {
            ArrayList<String> cameraIDs = getCameraIDs();
            camerasList.setModel(getListModel(cameraIDs));
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
            rateLabel.setText("Hourly rate: "+getRate());
        }

        private void updateGateTime() {
            gateTimeLabel.setText("Gate open time: "+getGateOpenTime()+"s");
        }

        // more helper methods

        private int getAvailability() {
            sendMessage("va");
            Message response = getMessage();
            return Integer.parseInt(response.getText().substring("va:".length()));
        }

        private String getAllIDsAsString(String command) {
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
            return getIDsFromString(getAllIDsAsString("vv"));
        }

        private ArrayList<String> getCameraIDs() {
            return getIDsFromString(getAllIDsAsString("vc"));
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
            sendMessage("gr");
            Message response = getMessage();
            return formatAmountString(response.getText().substring("gr:".length()));
        }

        private double getGateOpenTime() {
            sendMessage("go");
            Message response = getMessage();
            return Double.parseDouble(response.getText().substring("go:".length()));
        }
    }
}
