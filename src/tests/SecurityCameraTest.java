package tests;

import server.Garage;
import server.SecurityCamera;

public class SecurityCameraTest {
    @Test
    public void testSecurityCamera() {
        Garage g = new Garage();
	SecurityCamera cam = new SecurityCamera(g);

	assertNotNull(cam.view());
    }
}
