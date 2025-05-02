package interfaces;

import mock.Garage;
import mock.Receipt;
import server.UserType;

public interface UserInterface {
	void setGarage(Garage newGarage);
	Garage getGarage();
	UserType getType();
	String generateTicket(); // returns ticket ID of generated ticket
	Receipt payTicket(String ticketID); // returns receipt after successful payment
	int getAvailableSpaces(); // for va:<number>
	String viewBillingSummary(String ticketID); // for bs:<...>
	String getGarageName(); // for gn:<garage_name>
	void toggleGate(); // for tg:toggled
	boolean getGateStatus(); // for gs
}
