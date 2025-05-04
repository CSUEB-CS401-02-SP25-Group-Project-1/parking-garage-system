package shared;

import javax.swing.ImageIcon;

public class ImageMessage extends Message { // used to send over security camera feed
	private ImageIcon image;
	
	public ImageMessage(MessageType type, String sender, String text, ImageIcon image) {
		super(type, sender, text);
		this.image = image;
	}

	public ImageIcon getImage() {
		return image;
	}
}
