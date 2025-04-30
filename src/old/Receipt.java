package server;

public class Receipt 
{
	
	private String ticket_id;
	private	String garageName;
	private Date entryTime;
	private	Date exitTime;
	private double total_amount;

	public Receipt(Ticket ticket)
	{

		this.ticket_id = ticket.getID();
		this.garageName = ticket.getGarage().getName();
		this.entryTime = ticket.getEntryTime();
		this.exitTime = ticket.getExitTime();
		
		if (ticket.isClosed())
		{
			Date exit = ticket.getExitTime();
			Date entry = ticket.getExitTime();
			long hours_in_garage = (exit.getTime() - entry.getTime()) / 3600000;
			this.total_amount = hours_in_garage * ticket.getGarage().getRate();
		}
		
		else
		{
			this.total_amount = 0.0;
		}

	}
	
	public String toString()
	{

		String result = "RECEIPT\n";
		result += "Garage Name: " + garageName + "\n";
		result += "Ticket ID: " + ticket_id + "\n";
		result += "Entry Time: " + entryTime + "\n";
		result += "Exit Time: ";
		
		if (exitTime != null)
		{
			result += exitTime + "\n";
		}
		
		else
		{
			result += "Still parked in the garage!" + "\n";
		}
		
		result += "Total Amount: $" + total_amount + "\n";
		return result;
		
	}
}
