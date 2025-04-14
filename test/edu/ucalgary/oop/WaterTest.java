package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

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
    public void testCheckExpirationSameDay() {
        LocalDate today = LocalDate.now();

        testWater.checkExpiration(today);

        assertFalse("Water should not be expired on the same day it was allocated", testWater.getIsExpired());
    }

    @Test
    public void testCheckExpirationOneDayAfter() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        testWater.checkExpiration(yesterday);

        assertTrue("Water should be expired one day after it was allocated", testWater.getIsExpired());
    }

    @Test
    public void testCheckExpirationTwoDaysAfter() {
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

        testWater.checkExpiration(twoDaysAgo);

        assertTrue("Water should be expired two days after it was allocated", testWater.getIsExpired());
    }
}