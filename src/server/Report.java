
package server;

import interfaces.ReportInterface;

import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Enumeration;
import java.lang.NullPointerException;

public class Report implements ReportInterface {
	private ArrayList<Date> entryTimes;
	private ArrayList<Earning> earnings;
	private Garage garage;

	private static int count = 0;
	private String id;

	private int currentlyParked;

	private final int ms_p_h = 3600000;
	
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
		if (earnings.size() == 0) return 0;
		int year = (new Date()).getYear();
		double result = 0;
		int i = earnings.size()-1;
		while (i >= 0) {
			if (year - earnings.get(i).getDate().getYear() >= 1) break;
			result += earnings.get(i).getRevenue();
			i--;
		}
		
		return result;
	}

	public double getTotalRevenue() {
		double revenue = 0;
		for (Earning earning : earnings) {
			revenue += earning.getRevenue();
		}
		return revenue;
	}

	public String getPeakHour() {

		ConcurrentHashMap<Integer, Integer> entries_per_hour
			= new ConcurrentHashMap<>();
		for (Date d : entryTimes) {
			try {
				int count = entries_per_hour.get(d.getHours());
			} catch (NullPointerException n) {
				entries_per_hour.put(d.getHours(), 1);
				continue;
			}
			entries_per_hour.put(d.getHours(), count++);
		}
		int max_hour = 0;
		int max_entries = 0;
		
		Enumeration<Integer> keys = entries_per_hour.keys();
		
		while (keys.hasMoreElements()) {
			Integer hour = (Integer)keys.nextElement();
			Integer entries = entries_per_hour.get(hour);
			if (entries > max_entries) {
				max_entries = entries;
				max_hour = hour;
			}
		}
		return "" + max_hour;
	}

	public String toString() {
		// 3 lines:
		// Garage ID
		// Entries (comma-separated longs)
		// Earnings
	    String garageID = garage.getID();

	    
	    String entries_s = "";
	    for (Date entry : entryTimes) {
	        if (!entries_s.isEmpty()) entries_s += ",";
	        entries_s += entry.getTime();
	    }
	    
	    String earnings_s = "";
	    
	    for (Earning earning : earnings) {
	        if (!earnings_s.isEmpty()) earnings_s += "|";
	        earnings_s += earning.toString();
	    }
	    return garageID + "\n" + entries_s + "\n" + earnings_s;
	}
	
	// helper methods
	private double accumulate(long ms) {
		double revenue = 0;

		int i = earnings.size() - 1;
		long now = (new Date()).getTime();
		
		long hr_diff;

		while (i >= 0) {
			hr_diff = now - earnings.get(i).getDate().getTime();
			if (Long.compareUnsigned(hr_diff, ms) > 0) {
				break;
			}
			revenue += earnings.get(i).getRevenue();
			i--;
		}
		
		return revenue;
	}
}
