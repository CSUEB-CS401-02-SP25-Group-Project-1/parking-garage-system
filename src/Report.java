package server;

// Date format is:
// <Wkd> <Mth> <Day> <hr>:<mn>:<ss> <TMZ> <YEAR>

public class Report {
	private float[] hourlyEarnings;	// tracks money earned
	private int[] hourlyEntries; 	// tracks # people entered
	private int[] mostParked; 	// tracks most # people in garage at once

	private int hourIndex;		// points to current hour in arrays
	private Date created;		// hourIndex is updated accoring to (now - created) / 3600000; now - created gives time elapsed in milliseconds

	//Some constants that go along with hour calculations
	private final long millies_per_hour = 1 * 60 * 60 * 1000;
	private final long millies_per_halfhour = millies_per_hour / 2;

	private double totalEarned;	// total earned by this garage
	private long totalEntered;	// total entered this garage
	private int maxParked;		// most amount of cars in the garage ever

	private int currentlyParked;	// # currently parked

	public Report(){
		hourIndex = 0;
		// use `created` as reference for all hourIndex calcs
		created = new Date();

		hourlyEarnings = new float[365 * 24];
		hourlyEntries = new int[365 * 24];
		maxParked = new int[365 * 24];

		totalEarned = 0;
		totalEntered = 0;
		currentlyParked = 0;
	}

	public addEntry() {

		// Used for tracking a car entering the garage
		// hourIndex is updated every iteration. Similar runtime to using 'if' statements (division is slightly longer but who cares)
		// Update amount of cars in the garage
		// Check if there is a peak occupancy
		// Update total cars entered
		// Update cars entered this hour
		// Revenue is not tracked in this function

		Date now = new Date();
		long hours_elapsed = (now.getTime() - created.getTime()) / millies_per_hour; // 'long' type is 64 bits, which can track differences over 100,000,000 years
		hourIndex = diff_hr;

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

	public addExit(float amount) {

		// Used for tracking a car exiting to leave the garage
		// Customer leaves by paying
		// hourIndex is updated every iteration. Similar runtime to using 'if' statements (division is slightly longer but who cares)
		// Update amount of cars in the garage
		// No possibility of peak occupancy
		// Update revenue earned this hour
		// Update total revenue earned

		Date now = new Date();
		long hours_elapsed = (now.getTime() - created.getTime()) / millies_per_hour; // 'long' type is 64 bits, which can track differences over 100,000,000 years
		hourIndex = diff_hr;

		currentlyParked--;

		hourlyRevenue[hourIndex] += amount;

		totalRevenue += amount;
	}


	public String toString() {
		// Gives all states, regardless of what they might give.
		String rs;

		rs += lastHour();
		rs += lastToday();
		rs += lastWeek();
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
		rs += "\tCurrentlyParked: " currentlyParked + '\n';

		// Break for formatting
		rs += "\n'

		rs += "Last Hour:\n"
		rs += "\tEarned: $" + hourlyEarnings[hourlyIndex - 1] + '\n';
		rs += "\tEntered: " + hourlyEntries[hourlyIndex - 1] + '\n';
		rs += "\tMax parked at once: " maxParked[hourlyIndex - 1]" + '\n';

		return rs;
	}

	public String lastToday() {
		
		// Returns the data for the last 24 hours, from this hour (weighted weirdly if you are at beginning/end of current hour.

		Date now = new Date();
		int hours = round(24, now);

		long[3] stats = calculateTotals(hours); // calculateTotals returns {earnings, entries, max}

		String rs;

		rs += "\nLast 24 Hours:\n"
		rs += "\tEarned: $" + stats[0] + '\n';
		rs += "\tEntered: " + stats[1] + '\n';
		rs += "\tMax parked at once: "stats[2] + '\n';

		return rs;
	}

	public String lastWeek() {
		
		// Returns data for the last 24 * 7 = 168 hours

		Date now = new Date();
		int hours = round(168, now);

		long[3] stats = calculateTotals(hour);

		String rs;

		rs += "\nLast Week:\n"
		rs += "\tEarned: $" + earnings + '\n';
		rs += "\tEntered: " + entries + '\n';
		rs += "\tMax parked at once: " max + '\n';

		return rs;
	}

	public String lastYear() {
		
		//Returns data of the last 24 * 365 = 8760 hours

		Date now = new Date();

		int hours = round(8760, now);

		long[3] stats = calculateTotals(hours)

		String rs;

		rs += "\nLast Year:\n"
		rs += "\tEarned: $" + earnings + '\n';
		rs += "\tEntered: " + entries + '\n';
		rs += "\tMax parked at once: " max + '\n';

		return rs;
	
	}

	public String total() {
	
		String rs;

		rs += "\nTotal:\n"
		rs += "\tEarned: $" + totalEarned + '\n';
		rs += "\tEntered: " + totalEntered + '\n';
		rs += "\tMax parked at once: " + maxParked + '\n';

		return rs;
	}

	private int round(int hours, Date now) {

		// round() checks if the user has passed the halfway point of the current hour
		// This is when the earnings of the current hour would be substantial enough to not count an extra hour
		
		if ((now.getTime() - created.getTime() % (millies_per_hour)) > millies_per_halfhour) {
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