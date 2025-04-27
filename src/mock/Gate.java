package mock;

import interfaces.GateInterface;

public class Gate implements GateInterface {
	int openTime; // in seconds
	boolean open = false;
	
	public Gate(int openTime) {
		this.openTime = openTime;
	}
	
	public void open() {
		open = true;
	}

	public void close() {
		open = false;
	}

	public void setOpenTime(int seconds) {
		openTime = seconds;
	}

	public int getOpenTime() {
		return openTime;
	}
	
	public boolean isOpen() {
		return open;
	}

}
