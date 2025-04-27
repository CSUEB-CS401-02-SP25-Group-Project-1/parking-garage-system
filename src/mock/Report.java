package mock;

import java.util.ArrayList;
import java.util.Date;
import interfaces.ReportInterface;

public class Report implements ReportInterface {
	private ArrayList<Date> entryTimes;
	private ArrayList<Earning> earnings;
	private int currentlyParked; // number of currently parked vehicles in garage

	public void addEntryTime(Date entryTime) {
		entryTimes.add(entryTime);
	}

	public void addExit(Date exitTime, double amount) {
		earnings.add(new Earning(exitTime, amount));
	}

	public ArrayList<Date> getEntryTimes() {
		return entryTimes;
	}

	public double getRevenueThisHour() {
		// do date comparison of each earning in list and return accumulated value
		return 5000000.33; // dummy value
	}

	public double getRevenueToday() {
		// do date comparison of each earning in list and return accumulated value
		return 6666666.69; // dummy value
	}

	public double getRevenueThisWeek() {
		// do date comparison of each earning in list and return accumulated value
		return 9999999.99; // dummy value
	}

	public double getRevenueThisYear() {
		// do date comparison of each earning in list and return accumulated value
		return 150000000.00; // dummy value
	}

	public double getTotalRevenue() {
		// do date comparison of each earning in list and return accumulated value
		return 2000000000.41; // dummy value
	}
	
	public void setCurrentlyParkedNum(int currentlyParked) {
		this.currentlyParked = currentlyParked;
	}

	public int getCurrentlyParkedNum() {
		return currentlyParked;
	}

	public String getPeakHour() {
		// do date comparison of entry dates to get the peak parking hour
		return "10pm";
	}

	public ArrayList<Earning> getEarnings() {
		return earnings;
	}
	
	public String toString() {
		// used to dump all report data to file (both arrays)
		return "LONG LINES OF EARNINGS AND ENTRY TIMES";
	}
}
