package interfaces;

public interface GateInterface 
{
	void open();
	void close();
	void setopen_time(int seconds);
	int getopen_time();
	boolean isOpen();
}
