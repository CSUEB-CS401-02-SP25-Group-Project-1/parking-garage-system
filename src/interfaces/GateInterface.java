package interfaces;

import server.Garage;

public interface GateInterface {
	void open();
	void close();
	void setOpenTime(double seconds);
	double getOpenTime();
	boolean isOpen();
	Garage getGarage();
}
