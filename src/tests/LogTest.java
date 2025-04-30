package tests;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import org.junit.*;
import server.Log;
import server.LogType;

public class LogTest {
	@Test
	public void testFilename() {
		// log file should be created in current directory
		Log temp = new Log();
		File file = new File(temp.getFilepath());
		assertTrue(file.exists());
		
		// log file should be created in specified directory
		String dir = "testDir";
		temp = new Log(dir);
		file = new File(temp.getFilepath());
		assertTrue(file.exists());
		
		// log file should be created with prefix
		String prefix = "testPrefix_";
		temp = new Log(dir, prefix);
		String filename = temp.getFilepath();
		assertTrue(filename.contains(prefix));
	}
	
	@Test
	public void testAppend() {
		// new entry should be added to log
		String myText = "FOO"; // NOTE: all text in log entries are capitalized
		Log temp = new Log();
		temp.append(myText);
		String entryFromLog = temp.getEntries().get(0);
		assertTrue(entryFromLog.contains(myText));
		
		// if a log has multiple entries, the latest entry should be added at the end of the log
		temp.append("BAR");
		temp.append("STOOL");
		temp.append("SMILE");
		String testText = "BURGER";
		temp.append(testText);
		String returned = temp.getEntries().getLast();
		assertTrue(returned.contains(testText));
		
		// if log type has been specified, it should appear in the log entry
		temp.append(LogType.ACTION, "TEST");
		returned = temp.getEntries().getLast();
		assertTrue(returned.contains(LogType.ACTION.toString()));
		
		temp.append(LogType.ENTRY, "TEST");
		returned = temp.getEntries().getLast();
		assertTrue(returned.contains(LogType.ENTRY.toString()));
		
		temp.append(LogType.ERROR, "TEST");
		returned = temp.getEntries().getLast();
		assertTrue(returned.contains(LogType.ERROR.toString()));
	}
	
	@Test
	public void testGetEntries() throws InterruptedException {
		// getEntries should return all entries from log (without specifying date range)
		String[] testStrings = {"SOMETHING", "ABOUT", "TOAST", "AND", "WAFFLES"};
		Log temp = new Log();
		for (String str : testStrings) {
			temp.append(str);
		}
		ArrayList<String> entries = temp.getEntries();
		for (int i = 0; i < entries.size(); i++) {
			assertTrue(entries.get(i).contains(testStrings[i]));
		}
		
		// getEntries should return all entries logged after a certain date
		Date givenDate = new Date();
		Thread.sleep(1000); // some delay for the time check to work
		String[] newStrings = {"I", "COME", "FROM", "THE", "FUTURE"};
		for (String str : newStrings) {
			temp.append(str);
		}
		ArrayList<String> newEntries = temp.getEntries(givenDate);
		for (int i = 0; i < newEntries.size(); i++) {
			assertTrue(newEntries.get(i).contains(newStrings[i]));
		}
		
		// getEntries should return all entries logged within a certain date range
		Date earliest = new Date();
		Thread.sleep(1000); // some delay for the date comparison to work
		String[] inbetweenStrings = {"RELEVANT", "STRINGS", "DO", "YOUR", "WORST"};
		for (String str : inbetweenStrings) {
			temp.append(str);
		}
		Date latest = new Date();
		Thread.sleep(1000);
		String[] afterwards = {"BLAH", "BLAH", "BLAH"};
		for (String str : afterwards) {
			temp.append(str);
		}
		ArrayList<String> inbetweenEntries = temp.getEntries(earliest, latest);
		for (int i = 0; i < inbetweenEntries.size(); i++) {
			assertTrue(inbetweenEntries.get(i).contains(inbetweenStrings[i]));
		}
		
		// getEntries should return nothing if the date ranges are swapped ("earliest" is later than "latest")
		assertTrue(temp.getEntries(latest, earliest).isEmpty());
	}
}
