package server;

import interfaces.GateInterface;

public class Gate implements GateInterface {
	double openTime; // in seconds
	boolean open = false;
	Garage garage;
	
	public Gate(Garage garage, double openTime) {
		this.garage = garage;
		this.openTime = openTime;
	}
	
	public void open() {
		open = true;
	}

	public void close() {
		open = false;
	}

	public void setOpenTime(double seconds) {
		openTime = seconds;
	}

	public double getOpenTime() {
		return openTime;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public Garage getGarage() {
		return garage;
	}

}
