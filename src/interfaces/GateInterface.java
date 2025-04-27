package interfaces;

public interface GateInterface {
	void open();
	void close();
	void setOpenTime(int seconds);
	int getOpenTime();
	boolean isOpen();
}
