package server;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.*;
import java.util.*;

public class Log {
	private String path;
	private BufferedWriter fileWriter; // buffered so that new entries are written to file immediately
	private ArrayList<String> entries = new ArrayList<String>();
	
	public Log(String dir, String prefix) {
		setPath(dir, prefix);
		createFile();
	}
	
	public Log(String dir) { // constructor without specifying prefix
		setPath(dir, ""); // prefix would be blank
		createFile();
	}
	
	public Log() { // constructor without specifying a directory or prefix
		setPath("", ""); // would be current directory and blank prefix
		createFile();
	}

	public String getFilepath() {
		return path;
	}
	
	public void append(LogType logType, String text) { // add new entry to log (and also writes it to file)
		String entry = format(logType, text);
		entries.add(entry);
		writeEntry(entry);
		System.out.println(entry); // also prints entry to console
	}
	
	public void append(String text) { // append method without specifying a log type
		append(LogType.ACTION, text); // default type will be ACTION in this case
	}
	
	public ArrayList<String> getEntries(Date earliest, Date latest) { // returns all log entries from date range
		ArrayList<String> retrieved = new ArrayList<String>();
		if (earliest.after(latest)) { // return nothing if "earliest" is later than "latest" timestamp
			return retrieved;
		}
		for (String entry : entries) {
			Date entryDate = getEntryDate(entry);
			if (entryDate.before(earliest)) { // ignore entries that are way earlier in time
				continue;
			}
			if (entryDate.after(latest)) { // stop iterating once entries are beyond date range
				break;
			}
			retrieved.add(entry);
		}
		return retrieved;
	}
	
	public ArrayList<String> getEntries(Date earliest) { // returns all log entries after a certain date
		ArrayList<String> retrieved = new ArrayList<String>();
		for (String entry : entries) {
			Date entryDate = getEntryDate(entry);
			if (entryDate.before(earliest)) { // ignore entries that are way earlier in time
				continue;
			}
			retrieved.add(entry);
		}
		return retrieved;
	}
	
	public ArrayList<String> getEntries() { // returns all entries if no date range is specified
		return entries;
	}

	// helper methods
	private void setPath(String dir, String prefix) { // sets log file path
		Date curTime = new Date();
		String filename = prefix+curTime.getTime()+".log";
		Path temp = Paths.get(dir, filename); // properly concatenates into a full file path
		path = temp.toString();
	}
	
	private void createFile() { // creates file (and directory if it doesn't exist)
		File file = new File(path);
		// create directory (if it doesn't exist)
		File dir = file.getParentFile(); // gets directory of file
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		// create log file
		try {
			if (file.createNewFile()) {
				fileWriter = new BufferedWriter(new FileWriter(path)); // file writer is now usable throughout log's lifespan
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String format(LogType logType, String text) { // formats log entry
		String formattedText = "["+logType+"] ["+(new Date()).toString()+"] "+text.toUpperCase(); // text will be in all caps
		return formattedText;
	}
	
	private void writeEntry(String entry) { // writes entry to file
		try {
			fileWriter.write(entry+"\n");
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Date getEntryDate(String entry) { // returns date from string (as a string)
		String[] split = entry.split("\\] \\["); 
		String[] split2 = split[1].split("\\]"); // eliminate left end of string
		String dateStr = split2[0]; // eliminate right end of string
		SimpleDateFormat dateParser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); // converts string back into date object
		Date entryDate = null;
		try {
			entryDate = dateParser.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
		return entryDate;
	}
}
