package server;

import interfaces.GateInterface;
import server.Garage;

public class Gate implements GateInterface {
	private double openTime;
	private boolean isOpen;
	private Garage garage;

	// constructors

	public Gate() {
		garage = new Garage();
		openTime = 0;
		isOpen = false;
	}

	public Gate(Garage garage) {
		// if for any reason this happens
		this.garage = garage;
		openTime = 0;
		isOpen = false;
	}

	public Gate(Garage garage, double openTime) {
		this.garage = garage;
		this.openTime = openTime;
		isOpen = false;
	}

	// getters
	public double getOpenTime() {return openTime;}
	public boolean isOpen() {return isOpen;}
	public Garage getGarage() {return garage;}

	// setters
	public void setOpenTime (double seconds) {openTime = seconds;}

	// methods
	public void open() {isOpen = true;}
	public void close() {isOpen = false;}
}
