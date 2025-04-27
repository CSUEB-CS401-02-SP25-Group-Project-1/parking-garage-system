package interfaces;

import java.util.ArrayList;
import java.util.Date;
import server.Earning;

public interface ReportInterface {
	void addEntryTime(Date entryTime); // adds entry time to list, but does not create new Earning object
	void addExit(Date exitTime, double amount); // adds exit time and payment amount to new Earning object, this Earning also gets added to a list of Earnings
	ArrayList<Date> getEntryTimes();
	ArrayList<Earning> getEarnings();
	double getRevenueThisHour();
	double getRevenueToday();
	double getRevenueThisWeek();
	double getRevenueThisYear();
	double getTotalRevenue();
	int getCurrentlyParkedNum();
	String getPeakHour(); // returns peak hour based on entry times
	String toString();
}
