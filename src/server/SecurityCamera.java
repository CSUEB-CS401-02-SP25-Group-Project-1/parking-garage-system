package server;

import javax.swing.ImageIcon;

import interfaces.SecurityCameraInterface;

public class SecurityCamera implements SecurityCameraInterface {
	private static int count = 0;
	private String id;
	private Garage garage;
	
	public SecurityCamera(Garage garage) {
		id = "SC"+count++;
	}

	public ImageIcon view() {
		// returns a static image of a parking garage
		return null;
	}
	
	public String getID() {
		return id;
	}
	
	public String toString() {
		return garage.getID();
	}
}
