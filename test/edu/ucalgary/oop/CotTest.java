package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CotTest {
    private Cot testCot;

    @Before
    public void setUp() {
        testCot = new Cot(200, "110 B3");
    }

    @Test
    public void testCotConstructor() {
        assertEquals("Supply ID should be set correctly", 200, testCot.getSupplyId());
        assertEquals("Type should be 'Cot'", "Cot", testCot.getType());
        assertEquals("testCot location should be set correctly", "110 B3", testCot.getCotLocation());
    }

    @Test
    public void testGetCotLocation() {
        assertEquals("getCotLocation() should return the correct location", "110 B3", testCot.getCotLocation());
    }

    @Test
    public void testSetCotLocation() {
        testCot.setCotLocation("98 A2");
        assertEquals("testCot location should be updated to 'Room B'", "98 A2", testCot.getCotLocation());
    }

    @Test
    public void testSetCotLocationToEmpty() {
        boolean success;

        success = testCot.setCotLocation("");

        assertFalse("An empty cot location should not be possible", success);
    }
}
