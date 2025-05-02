package server;

import java.io.File;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import mock.Garage;

public class SecurityCamera {
    private final String FEED_DIR = "live-feed";
	private static int count = 0;
	private String id;
	private Garage garage;
	
	public SecurityCamera(Garage garage) {
		id = "SC"+count++;
		this.garage = garage;
	}

	public ImageIcon view() {
		// returns a static image of a parking garage
        return getLiveFeed();
	}
	
	public String getID() {
		return id;
	}
	
	public String toString() {
		return garage.getID();
	}

    private ImageIcon getLiveFeed() {
        String fullDir = Paths.get(System.getProperty("user.dir"), FEED_DIR).toString();
        File feedFolder = new File(fullDir);
        int cameraNum = Integer.parseInt(id.substring("SC".length()));
        String imagePath = Paths.get(fullDir, "feed_"+(cameraNum % feedFolder.listFiles().length)+".jpg").toString();
		File imageFile = new File(imagePath);
		if (!imageFile.exists()) {
			return null;
		}
        return new ImageIcon(imagePath);
    }
}
