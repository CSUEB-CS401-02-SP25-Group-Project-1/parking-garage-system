package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class Log {
	String path;
	public Log(String dir, String prefix) {
		setPath(dir, prefix);
		createFile();
	}
	
	public String getFilepath() {
		return path;
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
				FileWriter fileWriter = new FileWriter(path);
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
