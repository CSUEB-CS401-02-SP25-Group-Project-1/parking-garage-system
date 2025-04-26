package server;

public class Receipt 
{
	
	private Ticket ticket;
	
	public Receipt(Ticket ticket)
	{
		this.ticket = ticket;
	}
	
	public String toString()
	{
		String result = "Ticket ID: " + ticket.getID() + "\n";
		
		result += "Garage: " + ticket.getGarage().getName() + "\n";
		
		result += "Entry Time" + ticket.getEntryTime() + "\n";
		
		if(ticket.getExitTime() != null)
		{
			result += "Exit Time: " + ticket.getExitTime() + "\n";
		}
		
		result += "Status: ";
		
		if(ticket.isClosed())
		{
			result += "Currently Closed!";
		}
		
		else
		{
			result += "It's Open!";
		}
		
		if(ticket.isOverridden())
		{
			result += "Overridden";
		}
		
		result += "\n";
		
		return result;
	}
}
