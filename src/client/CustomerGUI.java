package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import shared.*;

public class CustomerGUI {
    private static final int PORT = 7777;
    private static String serverIP; // ip is preserved for reconnections
    private static String garageID; // id is preserved for reconnections
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
            mainMenuScreen();
            return;
        }
    }

    private static void mainMenuScreen() {
        JFrame window = new JFrame("Parking Garage System");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(560, 400);
        window.setLocationRelativeTo(null);
        window.setLayout(new GridBagLayout()); // main layout
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(10, 10, 10, 10); // padding
    
        // welcome label
        JLabel welcomeLabel = new JLabel("Welcome to "+getGarageName()+" ("+garageID+")");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        grid.gridx = 0;
        grid.gridy = 0;
        grid.gridwidth = 2;
        grid.anchor = GridBagConstraints.CENTER;
        window.add(welcomeLabel, grid);

        // availability label
        JLabel availabilityLabel = new JLabel();
        availabilityLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        grid.gridy = 1;
        window.add(availabilityLabel, grid);
        
        // buttons
        JButton getTicketButton = new JButton("Get Parking Ticket");
        grid.gridwidth = 1;
        grid.gridy = 2;
        grid.gridx = 0;
        window.add(getTicketButton, grid);
        
        JButton payTicketButton = new JButton("Pay Parking Fee");
        grid.gridx = 1;
        window.add(payTicketButton, grid);
        
        JButton viewSpacesButton = new JButton("View Available Spaces");
        grid.gridy = 3;
        grid.gridx = 0;
        window.add(viewSpacesButton, grid);
        
        JButton exitButton = new JButton("Exit");
        grid.gridx = 1;
        window.add(exitButton, grid);

        // update availability label
        updateAvailability(availabilityLabel);

        // button actions
        getTicketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateTicket(window, availabilityLabel);
            }
        });

        payTicketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                payTicket(window);
            }
        });

        viewSpacesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAvailability(availabilityLabel);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        // "closing window" procedure
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        // draw window
        window.setVisible(true);
    }

    // command methods

    private static void generateTicket(JFrame window, JLabel availabilityLabel) {
        sendMessage("gt");
        Message response = getMessage();
        if (response.getText().equals("gt:unavailable_space")) {
            JOptionPane.showMessageDialog(window, "Unable to generate ticket: no more space in garage", 
                                      "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String ticketID = response.getText().substring("gt:".length());
        JOptionPane.showMessageDialog(window, "Generated ticket: "+ticketID+
                                      "\nPlease keep this ticket for payment when exiting.",
                                      "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        updateAvailability(availabilityLabel);
    }

    private static void payTicket(JFrame window) {
        String ticketID = JOptionPane.showInputDialog(window, "Enter your ticket ID:");
        if (ticketID == null) { // user cancellation
            return;
        }
        ticketID = ticketID.toUpperCase();
        
        // get billing summary
        Double paymentAmount = displayBillingSummary(window, ticketID);
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
        if (!response.getText().startsWith("pt:")) {
            JOptionPane.showMessageDialog(window, "An unknown error has occurred",
                                      "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // view receipt
        String receipt = response.getText().substring("pt:".length());
        viewReceipt(window, receipt);
    }

    private static void updateAvailability(JLabel label) {
        sendMessage("va");
        Message response = getMessage();
        int available = Integer.parseInt(response.getText().substring("va:".length()));
        label.setText("Available spaces: " + available);
    }

    // helper methods

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
        String prompt = "Billing summary for ticket:\n"+
                        "Ticket ID: "+split[0]+"\n"+
                        "Entry time: "+new Date(Long.parseLong(split[1]))+"\n"+
                        "Exit time: "+new Date(Long.parseLong(split[2]))+"\n"+
                        "Total due: "+formatAmountString(split[3]);
        Object[] options = {"Pay", "Cancel"};
        int paymentPrompt = JOptionPane.showOptionDialog(
            window,
            prompt,
            "Payment Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        if (paymentPrompt != 0) {
            return null;
        }
        return paymentAmount;
    }

    private static void viewReceipt(JFrame window, String receipt) {
        JOptionPane.showMessageDialog(window, receipt, "Receipt",
                                  JOptionPane.PLAIN_MESSAGE);
    }

    private static String formatAmountString(String amountStr) {
        double rawValue = Double.parseDouble(amountStr);
        return "$"+String.format("%.2f", rawValue);
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
        closeConnection();
        System.exit(0);
    }

    private static void sendMessage(String text) {
        Message message = new Message(MessageType.Request, "customerClient", text);
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
        } catch (Exception e) {
            serverIP = null;
            return false;
        }
        return true;
    }

    private static boolean requestGarage(String id) {
        id = id.toUpperCase();
        sendMessage("init:"+id+":cu");
        Message response = getMessage();
        if (response.getText().equals("init:assigned_cu")) {
            return true;
        }
        garageID = null;
        return false;
    }
}
