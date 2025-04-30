package interfaces;

public interface GateInterface {
	void open();
	void close();
	void setOpenTime(double seconds);
	double getOpenTime();
	boolean isOpen();
}
