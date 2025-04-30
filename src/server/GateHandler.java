package server;

import mock.Gate;

public class GateHandler implements Runnable { // a class that keeps gate toggling logic running in another thread
	private final Gate gate;
	private final Log log;
	
	public GateHandler(Gate gate, Log log) {
		this.gate = gate;
		this.log = log;
	}
	
	public void run() {
		if (gate.isOpen()) {
			closeGate();
		} else {
			openGate();
		}
	}
	
	private void openGate() { // opens gate for a certain amount of time before automatically closing it
		gate.open();
		log.append(LogType.ACTION, "Gate is now opening in garage "+gate.getGarage().getID(), gate.getGarage());
		long millis = (long)(gate.getOpenTime() * 1000); // remains open for this amount of time before automatically closing
		try {
			Thread.sleep(millis); // wait
			closeGate();
		} catch (InterruptedException e) {
			log.append(LogType.ERROR, "Unable to close gate in garage "+gate.getGarage().getID(), gate.getGarage());
		} 
	}
	
	private void closeGate() {
		gate.close();
		log.append(LogType.ACTION, "Gate is now closing in garage "+gate.getGarage().getID(), gate.getGarage());
	}
}
