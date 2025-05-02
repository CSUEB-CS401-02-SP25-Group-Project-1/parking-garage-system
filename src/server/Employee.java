package server;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import interfaces.EmployeeInterface;
import server.UserType;

public class Employee extends User implements EmployeeInterface {
    private static int count = 0;
    private String id;
    private String username;
    private String password;
    
    public Employee(Garage garage, String username, String password) {
        id = "EM"+count++;
        this.username = username;
        this.password = password;
        this.garage = garage;
        this.userType = UserType.Employee;
    }

    public String getID() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    @Override
    public Receipt getReceipt(String ticketID) {
        Ticket ticket = garage.getTicket(ticketID);
        if (ticket == null || !ticket.isPaid()) {
            return null;
        }
        return new Receipt(ticket.getID(), garage.getName(), 
                         ticket.getEntryTime(), ticket.getExitTime(), 
                         ticket.getFee());
    }

    @Override
    public Report getGarageReport() {
        return garage.viewReport();
    }

    @Override
    public boolean changePassword(String newPassword) {
        if (newPassword.matches(".*[^a-zA-Z0-9].*")) {
            setPassword(newPassword);
            return true;
        }
        return false;
    }

    @Override
    public boolean modifyGateTime(double newTime) {
        if (newTime >= 0) {
            garage.getGate().setOpenTime(newTime);
            return true;
        }
        return false;
    }

    @Override
    public boolean overrideTicket(String ticketID, double newFee) {
        Ticket ticket = garage.getTicket(ticketID);
        if (ticket != null && newFee >= 0) {
            ticket.overrideFee(newFee);
            return true;
        }
        return false;
    }

    @Override
    public boolean modifyRate(double newRate) {
        if (newRate >= 0) {
            garage.setHourlyRate(newRate);
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Ticket> viewActiveTickets() {
        return garage.getActiveTickets();
    }

    @Override
    public ArrayList<String> viewCameraIDs() {
        ArrayList<String> cameraIDs = new ArrayList<>();
        for (SecurityCamera camera : garage.getCameras()) {
            cameraIDs.add(camera.getID());
        }
        return cameraIDs;
    }

    @Override
    public ImageIcon viewCameraFeed(String cameraID) {
        SecurityCamera camera = garage.getCameras().stream()
            .filter(c -> c.getID().equals(cameraID))
            .findFirst()
            .orElse(null);
        return camera != null ? camera.view() : null;
    }

    @Override
    public ArrayList<String> viewGarageLogs() {
        return garage.getLogEntries();
    }
    
    public String toString() {
        return garage.getID()+","+username+","+password;
    }
}
