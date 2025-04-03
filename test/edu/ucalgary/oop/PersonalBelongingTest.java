package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersonalBelongingTest {
    private PersonalBelonging testBelonging;

    @Before
    public void setUp() {
        testBelonging = new PersonalBelonging(100, "Test belonging");
    }

    @Test
    public void testPersonalBelongingConstructor() {
        assertEquals("testBelonging supply ID should be set correctly", 100, testBelonging.getSupplyId());
        assertEquals("Type should be 'Personal belonging'", "Personal belonging", testBelonging.getType());
        assertEquals("Description should be set correctly", "Test belonging", testBelonging.getDescription());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Description should be 'Test belonging'", "Test belonging", testBelonging.getDescription());
    }

    @Test
    public void testSetDescription() {
        testBelonging.setDescription("Jacket");
        assertEquals("Description should be set to 'Jacket'", "Jacket", testBelonging.getDescription());
    }

    @Test
    public void testSetDescriptionEmpty() {
        boolean success;

        success = testBelonging.setDescription("");

        assertFalse("Empty descriptions should not be allowed", success);
    }
}
