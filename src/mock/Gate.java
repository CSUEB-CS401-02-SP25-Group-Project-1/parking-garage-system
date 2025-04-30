package mock;

import interfaces.GateInterface;

public class Gate implements GateInterface {
	double openTime; // in seconds
	boolean open = false;
	
	public Gate(double openTime) {
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

}
