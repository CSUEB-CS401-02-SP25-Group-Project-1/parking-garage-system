package tests;

import server.Garage;
import server.SecurityCamera;
import static org.junit.Assert.*;
import org.junit.*;

public class SecurityCameraTest {
    @Test
    public void testConstructor() {
    	Garage garage = new Garage("Test_Garage", 1, 1, 1);
    	SecurityCamera camera = new SecurityCamera(garage);
    	
    	//Confirm Garage set properly
    	assertEquals(garage.getID(), camera.toString());
    }
    
    @Test
    public void testView() {
    	SecurityCamera camera = new SecurityCamera(new Garage());
    	
    	//Confirm that view doesn't return null
    	//Which would mean an error with getLiveFeed
    	assertTrue(camera.view() != null);
    }
}