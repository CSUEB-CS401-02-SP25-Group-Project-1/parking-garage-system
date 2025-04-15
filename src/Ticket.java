public class Ticket {
	private static int count = 0;

	private Garage garage;
	private Date entryTime;
	private Date exitTime;
	private bool isClosed;
	private int ID;

	public Ticket(Garage garage) {
		entryTime = new Date();
		exitTime = null;
		ID = ++count;
		isClosed = false;

		this.garage = garage;
	}

	public Garage getGarage() {return garage;}
	public Date getEntryTime() {return entryTime;}
	public Date getExitTime() {return exitTime;}
	public bool isClosed() {return isClosed;}
	public String getID() {return ("T" + ID);}

	public void close() {
		if (!isClosed) {
			exitTime = new Date();
			isClosed = true;
		}
	}
}