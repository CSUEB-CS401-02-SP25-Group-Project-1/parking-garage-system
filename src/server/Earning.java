package server;

import java.util.Date;
import interfaces.EarningInterface;

public class Earning implements EarningInterface {
	private Date date;
	private double revenue;
	
	public Earning(Date date, double revenue) {
		this.date = date;
		this.revenue = revenue;
	}

	public Date getDate() {
		return date;
	}
	
	public double getRevenue() {
		return revenue;
	}
	
	public String toString() {
		return date.getTime()+","+revenue;
	}
}
