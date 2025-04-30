package shared;

import java.io.Serializable;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class ImageMessage extends Message implements Serializable { 
	private ImageIcon image;
	
	public ImageMessage(MessageType type, String sender, String text, ImageIcon image) {
		super(type, sender, text);
		this.image = image;
	}

	public ImageIcon getImage() {
		return image;
	}
}
