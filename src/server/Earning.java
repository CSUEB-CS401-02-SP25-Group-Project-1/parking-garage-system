package server;

import java.util.Date;

public class Earning {
	// associates time customer left to amount they paid
	private Date date;
	private double revenue;

	// constructor
	public Earning(Date date, double revenue) {
		this.date = date;
		this.revenue = revenue;
	}

	// getters
	public Date getDate() {return date;}
	public double getRevenue() {return revenue;}

	public String toString() {
		String r = "";
		r += date.getTime() + "," + revenue;
		return r; 
	}
}
