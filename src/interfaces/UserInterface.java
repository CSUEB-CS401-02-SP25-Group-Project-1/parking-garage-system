package interfaces;

import server.Garage;
import server.Receipt;
import server.UserType;

public interface UserInterface {
	void setGarage(Garage newGarage);
	Garage getGarage();
	UserType getType();
	String generateTicket(); // returns ticket ID of generated ticket
	Receipt payTicket(String ticketID); // returns receipt after successful payment
}
