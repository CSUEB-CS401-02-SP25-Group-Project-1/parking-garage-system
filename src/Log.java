package server; //use server and client packages?
public class Log {
	private String log;

	public Log() {
		log = "";
		this.append(LogType.ENTRY, "Log created"); //append immediate creation entry 
	}

	public void append(String logMessage, LogType logType) { //use LogType? Or just append native string to Log?
		String s = ""
		switch (logType) { //will remove if different way of classifying log entries is agreed upon
			case LogType.ACTION: s += "ACTION:"; break;
			case LogType.ERROR: s += "ERROR:"; break;
			default: s += "ENTRY:"; break;
		}
		s += (new Date()).toString) + "::: " + message + "\n"; //entries conclude with newline character
		return;
	}

	public String getLog(Date earliest, Date latest) {

		//We ask for a Date object for consistent formatting
		//But everything is a string in our log, so:
		String sEarliest = earlist.toString();
		String sLatest = latest.toString();

		//Check if dates entered are valid
		if (sEarliest.comparedTo(sLatest) > 0) {
			// if earliest comes after latest
			return "Invalid Log request";
		}


		String[] entries = log.split('\n'); //split log into individual entries, each entry ends with newline
		String[] dates = new String[entries.length]; //make dates array of equal length for getting window
		
		for (int i = 0; i < entries.length; i++) {
			// Log entries in form: "<LogType>:<Date>:::<Message>"
			// Splitting at semicolon gives {<LogType>, <Date>, ..., <Message>}. Extra entries for 3 colon format
			// Index 1 holds the date String
			// Rest of log entry is discarded for this
			dates[i] = entries[i].split(':')[1];
		}

		// Now we have a list of entries and a list of dates.
		// We want to scan through the list of dates until we find one of our boundry points
		// Start from bottom of entry so we don't go through the whole history to begin with
		// Is there some way to turn earliest and latest into indeces?

		int i = entries.length - 1;

		// Check first if date entered is valid
		if (sLatest.comparedTo(dates[i]) > 0) {
			return "Invalid Log request.";
		}
		
		while (i >= 0 && dates[i].comparedTo(sLatest) > 0) {
			// i has to be within bounds of the array
			// i starts from the bottom (latest) and works backwards
			// if the entry at dates[i] is later than our latest date, keep working backwards
			// loop ends if beginning is reached or if the date of log entry is before/at asked time
			i--;
		}

		// Keep using i as index
		// Append proceeding log entries to return string
		String s = "";
		while (i >= 0 && dates[i].comparedTo(sEarliest) <= 0) {
			// i has to be within array bounds
			// will append things in Log until earliest date is found

			s += entries[i]; //appending entry as is will be suitable.
		}

		// Once here, our String is ready to be returned
		// A good programmer would clean up the temporary arrays

		return s;
	}
}