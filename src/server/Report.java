package server;

import interfaces.ReportInterface;

import java.util.Date;
import java.util.ArrayList;

public class Report implements ReportInterface {
	private ArrayList<Date> entryTimes;
	private ArrayList<Earning> earnings;
	private Garage garage;

	private static int count = 0;
	private String id;

	private int currentlyParked;

	private final int ms_p_h = 3600000;
	// needs:
		// addEntryTime(Date entryTime) // for ServerData loading
		// addExit(Date exitTime, double amount)
		// addExit(Earning earning) // what is an Earning?
		// getters:
			// getRevenueThisHour()
			// getRevenueToday()
			// getRevenueThisMonth()
			// getRevenueThisYear()
			// getTotalRevenue()
			// getGarage()
			// getID()
			// getCurrentlyParkedNum()
			// getPeakHour()
			// toString()
				// 3 lines:
				// Garage ID
				// Entries (comma-separated longs)
				// Earnings
				  // "exitTime,revenue\\|exitTime,revenue"
	
	// constructors
	public Report(Garage garage) {
		// new report created
		id = "R" + (count++);
		this.garage = garage;
		entryTimes = new ArrayList<>();
		earnings = new ArrayList<>();
		currentlyParked = 0;
	}

	public Report() {
		// no arguments constructor
		id = "R" + (count++);
		this.garage = new Garage();
		entryTimes = new ArrayList<>();
		earnings = new ArrayList<>();
		currentlyParked = 0;
	}

	public void addEntryTime(Date entryTime) {
		entryTimes.add(entryTime);
		currentlyParked++;
	}

	public void addExit(Earning earning) {	
		if (currentlyParked == 0) {return;}
		earnings.add(earning);
		currentlyParked--;
	}

	public void addExit(Date exitTime, double amount) {
		addExit(new Earning(exitTime, amount));
	}

	// setters
	public void setCurrentlyParkedNum(int currentlyParked) {
		this.currentlyParked = currentlyParked;
	}

	// getters
	public ArrayList<Date> getEntryTimes() {return entryTimes;}
	public ArrayList<Earning> getEarnings() {return earnings;}
	public Garage getGarage() {return garage;}
	public String getID() {return id;}
	public int getCurrentlyParkedNum() {return currentlyParked;}

	// methods
	public double getRevenueThisHour() {
		long ms = ms_p_h * 1; // 3,600,000 (ms/hr) * 1 hr	
		return accumulate(ms);
	}

	public double getRevenueToday() {
		long ms = ms_p_h * 24; // 24 hrs = 1 day
		return accumulate(ms);
	}

	public double getRevenueThisWeek() {
		long ms = ms_p_h * 24 * 7; // 1 day * 7 = week
		return accumulate(ms);
	}

	public double getRevenueThisMonth() {
		long ms = ms_p_h * 24 * 30; // 30-ish days a month
		return accumulate(ms);
	}

	public double getRevenueThisYear() {
		long ms = ms_p_h * 24 * 365; // 365 days a year
		return accumulate(ms);
	}

	public double getTotalRevenue() {
		double revenue;
		for (Earning earning : earnings) {
			revenue += earning.getRevenue();
		}
		return revenue;
	}

	public String getPeakHour() {
		// how to distinguish hours?

		// loop through all entries
		// subtract from base
		long rdate = entryTimes.get(0).getTime(); // date to be returned
		long max = 0;	// stores max value found
		long entries = 0;	// accumulates total entries per hour
		long baseDate = rdate;
			// used to subtract from proceeding dates
		long now;	// the current date in the list
		for (Date date : entryTimes) {
			now = date.getTime();
			if ((now - baseDate)/3600000 >= 1) {
				// change base date
				// check for maximum
				if (entries > max) {
					max = entries;
					rdate = baseDate;
				}

				// resest accumulating variable
				entries = 1; // count the current time
				baseDate = now;
			} else {
				entries++;
			}
		}
		return "" + rdate; // return casted date to String
	}

	public String toString() {
		// 3 lines:
		// Garage ID
		// Entries (comma-separated longs)
		// Earnings
			// "exitTime,revenue\\|exitTime,revenue"
		String garageID = garage.getID();

		String entries_s = "";
		String earnings_s = "";

		for (Date entry : entries) {
			entries_s += entry.getTime() + ",";
		}

		for (Earning earning : earnings) {
			earnings_s += earning.toString() + "\\|";
		}
		return garageID + "\n" + entries_s + "\n" + earnings_s + "\n";
	}
	
	// helper methods
	private double accumulate(long ms) {
		double revenue = 0;

		int i = earnings.size() - 1;
		long now = (new Date()).getTime();

		while (now - earnings.get(i).getDate().getTime() <= ms
				&& i >= 0) {
			revenue += earnings.get(i).getRevenue();
			i--;
		}
		
		return revenue;
	}
}
