package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class Log {
	String path;
	BufferedWriter fileWriter; // buffered so that new entries are written to file immediately
	ArrayList<String> entries = new ArrayList<String>();
	
	public Log(String dir, String prefix) {
		setPath(dir, prefix);
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
	
	public void append(String text) { // default append() method without specifying a log type
		append(LogType.ACTION, text); // default type will be ACTION in this case
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
		String formattedText = "[";
		switch (logType) {
			case LogType.ACTION: formattedText += "ACTION"; break;
			case LogType.ERROR: formattedText += "ERROR"; break;
			default: formattedText += "ENTRY"; break;
		}
		formattedText += "] [" + (new Date()).toString() + "] " + text.toUpperCase(); // text will be in all caps
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
}
