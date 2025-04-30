package server;

import interfaces.GateInterface;

public class Gate implements GateInterface
{
	private int open_time;
	private boolean open = false;
	
	public Gate(int open_time)
	{
		this.open_time = open_time;
	}
	
	public void open()
	{
		open = true;
	}
	
	public void close()
	{
		open = false;
	}
	
	public void setopen_time(int seconds)
	{
		this.open_time = seconds;
	}
	
	public int getopen_time()
	{
		return open_time;
	}
	
	public boolean isOpen()
	{
		return open;
	}
	
}
