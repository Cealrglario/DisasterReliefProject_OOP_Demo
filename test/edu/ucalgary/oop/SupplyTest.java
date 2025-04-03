package edu.ucalgary.oop;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class SupplyTest {
    private Supply testSupply;

    @Before
    public void setUp() {
        testSupply = new Blanket(100);
        testSupply.setComments("Test comment");
    }

    @Test
    public void testSupplyConstructor() {
        assertEquals("supplyId should be set properly", 100, testSupply.getSupplyId());
        assertEquals("type should be set properly", "Blanket", testSupply.getType());
    }

    @Test
    public void getSupplyId() {
        assertEquals("supplyId should be retrieved as expected", 100, testSupply.getSupplyId());
    }

    @Test
    public void getComments() {
        assertEquals("comments should be retrieved as expected", "Test comment",
                testSupply.getComments());
    }

    @Test
    public void setComments() {
        testSupply.setComments("New comment");

        assertEquals("comments should be set to 'New comment'", "New comment",
                testSupply.getComments());
    }

    @Test
    public void getType() {
        assertEquals("type should be retrieved as expected", "Blanket",
                testSupply.getType());
    }
}