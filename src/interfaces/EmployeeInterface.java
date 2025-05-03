package interfaces;

import mock.Receipt;
import mock.Report;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import mock.Ticket;

public interface EmployeeInterface {
	String getID();
	String getUsername();
	String getPassword();
	Receipt getReceipt(String ticketID); // returns receipt of an already paid-for ticket
	Report getGarageReport();
	boolean changePassword(String newPassword); // for mp:<newPassword>
	boolean modifyGateTime(double newTime); // for mg:<newTime>
	boolean overrideTicket(String ticketID, double newFee); // for ot:<ticketID>:<fee>
	boolean modifyRate(double newRate); // for mr:<newRate>
	ArrayList<Ticket> viewActiveTickets(); // for vv:<ticketIDs>
	ArrayList<String> viewCameraIDs(); // for vc:<cameraIDs>
	ImageIcon viewCameraFeed(String cameraID); // for vf:<cameraID>
	ArrayList<String> viewGarageLogs(); // for vl:<logText>
	double getGarageRate(); // for gr
}
