package server;

public class Report {
	private float[] hourlyEarnings;	// tracks money earned
	private int[] hourlyEntries; 	// tracks # people entered
	private int[] mostParked; 	// tracks most # people in garage at once

	private int hourIndex;		// points to current hour in arrays
	private Date created;		// hourIndex is updated accoring to (now - created) / 3600000; (now - created) gives time elapsed in milliseconds

	// Some constants that go along with hour calculations
	private final long millies_per_hour = 1 * 60 * 60 * 1000;
	private final long millies_per_halfhour = millies_per_hour / 2;

	private double totalEarned;	// total earned by this garage
	private long totalEntered;	// total entered this garage
	private int maxParked;		// most amount of cars in the garage ever

	private int currentlyParked;	// # currently parked

	public Report(){
		
		// use `created` as reference for all hourIndex calcs
		created = new Date();

		hourlyEarnings = new float[365 * 24];
		hourlyEntries = new int[365 * 24];
		maxParked = new int[365 * 24];

		totalEarned = 0;
		totalEntered = 0;
		currentlyParked = 0;
	}

	public void addEntry() {

		// Used for tracking a car entering the garage
		// hourIndex is updated every iteration. Similar runtime to using 'if' statements (division is slightly longer but who cares)
		// Update amount of cars in the garage
		// Check if there is a peak occupancy
		// Update total cars entered
		// Update cars entered this hour
		// Revenue is not tracked in this function

		hourIndex = updateHourIndex();

		// Increment number of cars in the garage
		currentlyParked++;

		// Check if there is a new peak # cars in garage
		if (currentlyParked > mostParked[hourIndex]) {
			maxOccupancies[hourIndex] = currentlyParked;
			if (mostParked[hourIndex] > maxParked) {
				maxParked = mostParked[hourIndex];
			}
		}

		// add entry to hourlyEntries
		hourlyEntries[hourIndex]++;

		// increment totalEntered
		totalEntered++; 
	}

	public void addExit(float amount) {

		// Used for tracking a car exiting to leave the garage
		// Customer leaves by paying
		// hourIndex is updated every iteration. Similar runtime to using 'if' statements (division is slightly longer but who cares)
		// Update amount of cars in the garage
		// No possibility of peak occupancy
		// Update revenue earned this hour
		// Update total revenue earned

		// Point to the current hour in arrays
		hourIndex = updateHourIndex();

		// Somebody left
		currentlyParked--;

		// add amount to revenue earned this hour
		hourlyRevenue[hourIndex] += amount;

		// at amount to total revenue
		totalRevenue += amount;
	}


	public String toString() {
		// String version of report inculdes all timeframes, 
			//hour, day, week, year
		//regardless of what they might return

		String rs;

		rs += lastHour();
		rs += lastToday();
		rs += lastWeek();
		rs += lastMonth();
		rs += lastYear();
		rs += total();

		return rs;

	}


	public String lastHour() {

		// Returns data for this hour and the last hour
		// Hard to decide which one to use and which one not to use, so included both

		String rs;

		rs += "This Hour:\n"
		rs += "\tEarned: $" + hourlyEarnings[hourlyIndex] + '\n';
		rs += "\tEntered: " + hourlyEntries[hourlyIndex] + '\n';
		rs += "\tCurrentlyParked: " + currentlyParked + '\n';

		// Break for formatting
		rs += "\n';

		rs += "Last Hour:\n";
		rs += "\tEarned: $" + hourlyEarnings[hourlyIndex - 1] + '\n';
		rs += "\tEntered: " + hourlyEntries[hourlyIndex - 1] + '\n';
		rs += "\tMax parked at once: " + maxParked[hourlyIndex - 1] + '\n';

		return rs;
	}

	public String lastToday() {
		
		// Returns the data for the last 24 hours, from this hour (weighted weirdly if you are at beginning/end of current hour.

		int hours = round(24);

		long[3] stats = calculateTotals(hours); // calculateTotals returns {earnings, entries, max}

		String rs;

		rs += "\nLast 24 Hours:\n"
		rs += "\tEarned: $" + stats[0] + '\n';
		rs += "\tEntered: " + stats[1] + '\n';
		rs += "\tMax parked at once: " + stats[2] + '\n';

		return rs;
	}

	public String lastWeek() {
		
		// Returns data for the last 24 * 7 = 168 hours

		Date now = new Date();
		int hours = round(168, now);

		long[3] stats = calculateTotals(hour);

		String rs;

		rs += "\nLast Week:\n";
		rs += "\tEarned: $" + stats[0] + '\n';
		rs += "\tEntered: " + stats[1] + '\n';
		rs += "\tMax parked at once: " + stats[2] + '\n';

		return rs;
	}

	public String lastMonth() {
		// Returns the data for the last 24 * 30 = 720 hours, from this hour (weighted weirdly if you are at beginning/end of current hour.

		int hours = round(720);

		long[3] stats = calculateTotals(hours); // calculateTotals returns {earnings, entries, max}

		String rs;

		rs += "\nLast Month:\n";
		rs += "\tEarned: $" + stats[0] + '\n';
		rs += "\tEntered: " + stats[1] + '\n';
		rs += "\tMax parked at once: " + stats[2] + '\n';

		return rs;
	}

	public String lastYear() {
		
		//Returns data of the last 24 * 365 = 8760 hours

		int hours = round(8760);

		long[3] stats = calculateTotals(hours); // calculateTotals() returns {earnings, entries, max}

		String rs;

		rs += "\nLast Year:\n";
		rs += "\tEarned: $" + stats[0] + '\n';
		rs += "\tEntered: " + stats[1] + '\n';
		rs += "\tMax parked at once: " + stats[2] + '\n';

		return rs;
	
	}

	public String total() {
	
		String rs;

		rs += "\nTotal:\n";
		rs += "\tEarned: $" + totalEarned + '\n';
		rs += "\tEntered: " + totalEntered + '\n';
		rs += "\tMax parked at once: " + maxParked + '\n';

		return rs;
	}

	private int updateHourIndex() {
		// Returns truncated number of hours since report creation

		Date now = new Date();
		return (now.getTime() - created.getTime()) / millies_per_hour;
	}

	private int round(int hours) {

		// round() checks if the user has passed the halfway point of the current hour
		// This is when the earnings of the current hour would be substantial enough to count as an extra hour, so `hours` should be decremented
		// Before the halfway point, we estimate that not enough time has passed for this hour to "count" towards total (even though it is still added)
		
		Date now = new Date();

		if ((now.getTime() - created.getTime() % millies_per_hour) > millies_per_halfhour) {
			hours--;
		}

		return hours;
	}

	private long[] calculateTotals(int window) {

		// calculateTotals is a commonly used process in this class, which accumulates stats of the Report's array attributes

		long earnings, entries, max = 0;

		for (int i = hourIndex; i >= (hourIndex - window) && i > 0; i--) {
			earnings += hourlyEarnings[i];
			entries += hourlyEntries[i];
			if (mostParked[i] > max) {
				max = maxParked[i];
			}
		}

		return {earnings, entries, max};
	}	
}