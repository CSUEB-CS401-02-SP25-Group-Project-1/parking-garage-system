package tests;

import static org.junit.Assert.assertTrue;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import org.junit.Test;
import shared.ImageMessage;

public class ImageMessageTest {
    @Test
    public void testImage() {
        ImageIcon image = new ImageIcon(Paths.get("live-feed", "feed_1.jpg").toString());
        ImageMessage temp = new ImageMessage(null, null, null, image);
        assertTrue(temp.getImage().equals(image));
    }
}
