package tests;

import server.Earning;
import static org.junit.Assert.*;
import java.util.Date;
import org.junit.*;

public class EarningTest {
    @Test
    public void testGettingDate() {
        Date date = new Date();
        Earning earning = new Earning(date, 0);
        assertEquals(date, earning.getDate());
    }

    @Test
    public void testGettingRevenue() {
        double revenue = 1234.56;
        Earning earning = new Earning(null, revenue);
        assertTrue(revenue == earning.getRevenue());
    }

    @Test
    public void testToString() {
        Date date = new Date();
        double revenue = 78910.11;
        Earning earning = new Earning(date, revenue);
        String expectedString = date.getTime()+","+revenue;
        assertEquals(expectedString, earning.toString());
    }
}
