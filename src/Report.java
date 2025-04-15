package server;

// Date format is:
// <Wkd> <Mth> <Day> <hr>:<mn>:<ss> <TMZ> <YEAR>

public class Report {
	private float[] hourlyEarnings;	// tracks money earned
	private int[] hourlyEntries; 	// tracks # people entered
	private int[] maxOccupancies; 	// tracks most # people in garage at once

	private int hourIndex;		// points to current hour in arrays
	private Date thisHour;		// Date of current hour. Used to check if hourIndex++

	private double totalEarned;	// total earned by this garage
	private long totalEntered;	// total entered this garage
	private int currentlyParked;	// # currently parked

	public Report(){
		hourIndex = 0;
		// Date format is:
		// <Wkd> <Mth> <Day> <hr>:<mn>:<ss> <TMZ> <YEAR>
		thisHour = new Date();

		hourlyEarnings = new float[365 * 24];
		hourlyEntries = new int[365 * 24];
		maxOccupancies = new int[365 * 24];

		totalEarned = 0;
		totalEntered = 0;
		currentlyParked = 0;
	}

}