package mock;

import java.util.Date;

import interfaces.ReceiptInterface;

public class Receipt implements ReceiptInterface {
	private String ticketID;
	private String garageName;
	private Date entryTime;
	private Date exitTime;
	private double paymentAmount;
	
	public Receipt(String ticketID, String garageName, Date entryTime, Date exitTime, double paymentAmount) {
		this.ticketID = ticketID;
		this.garageName = garageName;
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.paymentAmount = paymentAmount;
	}
	
	public String toString() {
		return "RECEIPT" // dummy value
				+ "garage name"
				+ "ticket id"
				+ "entry time"
				+ "exit time"
				+ "payment amount";
	}
}
