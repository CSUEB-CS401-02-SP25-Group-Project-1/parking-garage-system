package interfaces;

import java.util.ArrayList;
import java.util.Date;
import server.Earning;
import server.Garage;

public interface ReportInterface {
	void addEntryTime(Date entryTime); // adds entry time to list, but does not create new Earning object
	void addExit(Date exitTime, double amount); // adds exit time and payment amount to new Earning object, this Earning also gets added to a list of Earnings
	void addExit(Earning earning); // earnings can also be added directly
	ArrayList<Date> getEntryTimes();
	ArrayList<Earning> getEarnings();
	double getRevenueThisHour();
	double getRevenueToday();
	double getRevenueThisWeek();
	double getRevenueThisMonth();
	double getRevenueThisYear();
	double getTotalRevenue();
	Garage getGarage();
	String getID();
	void setCurrentlyParkedNum(int currentlyParked);
	int getCurrentlyParkedNum();
	String getPeakHour(); // returns peak hour based on entry times
	String toString();
}
