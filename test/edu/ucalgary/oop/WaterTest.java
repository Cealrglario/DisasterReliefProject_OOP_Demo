package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class WaterTest {
    private Water testWater;

    @Before
    public void setUp() {
        testWater = new Water(100, true);
    }

    @Test
    public void testWaterConstructor() {
        assertEquals("supplyId should be set correctly", 100, testWater.getSupplyId());
        assertEquals("type should be 'Water'", "Water", testWater.getType());
        assertTrue("allocatedToVictim should be true", testWater.getAllocatedToVictim());
        assertFalse("Water should not be expired initially", testWater.getIsExpired());
    }

    @Test
    public void testGetAllocatedToVictim() {
        assertTrue("getAllocatedToVictim should be retrieved as expected", testWater.getAllocatedToVictim());
    }

    @Test
    public void testSetAllocatedToVictim() {
        testWater.setAllocatedToVictim(false);
        assertFalse("allocatedToVictim should be set to false", testWater.getAllocatedToVictim());
    }

    @Test
    public void testGetIsExpired() {
        assertFalse("Water should not be expired initially", testWater.getIsExpired());
    }

    @Test
    public void testSetIsExpired() {
        testWater.setIsExpired(true);
        assertTrue("After setting, water should be expired", testWater.getIsExpired());
    }

    @Test
    public void testCheckExpirationBeforeThreshold() {
        // Assuming checkExpiration sets water as expired if currentTime is EXACTLY on or after January 1, 2025
        LocalDateTime beforeThreshold = LocalDateTime.of(2024, 12, 31, 23, 59);

        testWater.checkExpiration(beforeThreshold);

        assertFalse("Water should not be expired before threshold", testWater.getIsExpired());
    }

    @Test
    public void testCheckExpirationAtOrAfterThreshold() {
        LocalDateTime atThreshold = LocalDateTime.of(2025, 1, 1, 0, 0);

        testWater.checkExpiration(atThreshold);

        assertTrue("Water should be expired at or after threshold", testWater.getIsExpired());
    }

}
