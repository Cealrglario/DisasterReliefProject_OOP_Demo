package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class SupplyServiceTest {
    private SupplyService supplyService;
    private SupplyAccess<Object> supplyAccess;

    @Before
    public void setUp() {
        supplyService = SupplyService.INSTANCE;
        supplyAccess = new SupplyAccess<>();
    }

    @Test
    public void testGetSupplyById() {
        Supply testSupply = null;

        try {
            testSupply = supplyService.getSupplyById(1);
        } catch (SQLException e) {
            fail("Error testing getSupplyById: " + e.getMessage());
        }

        assertEquals("getSupplyById() should retrieve the correct Supply", 1, testSupply.getSupplyId());
    }

    @Test
    public void testGetAllSupplies() {
        List<Supply> retrievedSupplies = null;

        try {
            retrievedSupplies = supplyService.getAllSupplies();
        } catch (SQLException e) {
            fail("Error testing getAllSupplies: " + e.getMessage());
        }

        assertNotNull("getAllSupplies() should retrieve a valid list of Supplies", retrievedSupplies);
    }

    @Test
    public void testAddBlanketSupply() {
        Supply testSupply = null;
        String testType = "blanket";
        String testComments = "Test blanket comments";

        try {
            testSupply = supplyService.addSupply(testType, testComments);
        } catch (SQLException e) {
            fail("Error testing addSupply: " + e.getMessage());
        }

        assertNotNull("addSupply() should create and return a valid Supply", testSupply);
        assertEquals("addSupply() should set the type correctly", testType.toLowerCase(), testSupply.getType().toLowerCase());
        assertEquals("addSupply() should set the comments correctly", testComments, testSupply.getComments());

        try {
            supplyService.removeSupply(testSupply);
        } catch (SQLException e) {
            fail("Error testing addSupply: " + e.getMessage());
        }
    }

    @Test
    public void testAddCotSupply() {
        Supply testSupply = null;
        String testType = "cot";
        String testComments = "202 A12";

        try {
            testSupply = supplyService.addSupply(testType, testComments);
        } catch (SQLException e) {
            fail("Error testing addCotSupply: " + e.getMessage());
        }

        assertNotNull("addSupply() should create and return a valid Supply", testSupply);
        assertEquals("addSupply() should set the type correctly", testType.toLowerCase(), testSupply.getType().toLowerCase());
        assertEquals("addSupply() should set the comments correctly", testComments, testSupply.getComments());
        assertTrue("Supply should be an instance of Cot", testSupply instanceof Cot);

        try {
            supplyService.removeSupply(testSupply);
        } catch (SQLException e) {
            fail("Error testing addSupply: " + e.getMessage());
        }
    }

    @Test
    public void testAddWaterSupply() {
        Supply testSupply = null;
        String testType = "water";

        try {
            testSupply = supplyService.addSupply(testType, null);
        } catch (SQLException e) {
            fail("Error testing addWaterSupply: " + e.getMessage());
        }

        assertNotNull("addSupply() should create and return a valid Supply", testSupply);
        assertEquals("addSupply() should set the type correctly", testType.toLowerCase(), testSupply.getType().toLowerCase());
        assertTrue("Supply should be an instance of Water", testSupply instanceof Water);

        try {
            supplyService.removeSupply(testSupply);
        } catch (SQLException e) {
            fail("Error testing addWaterSupply: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateSupplyComments() {
        Supply testSupply = null;
        String retrievedComments = null;

        try {
            testSupply = supplyService.addSupply("blanket", "Initial comments");
            supplyService.updateSupplyComments(testSupply, "Updated comments");

            retrievedComments = (String) supplyAccess.getInfo("comments", testSupply.getSupplyId());
        } catch (SQLException e) {
            fail("Error testing updateSupplyComments: " + e.getMessage());
        }

        assertEquals("updateSupplyComments() should update comments in-memory and in database",
                testSupply.getComments(), retrievedComments);

        try {
            supplyService.removeSupply(testSupply);
        } catch (SQLException e) {
            fail("Error testing updateSupplyComments: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveSupply() {
        Supply testSupply = null;
        boolean removalResult = false;

        try {
            testSupply = supplyService.addSupply("blanket", "Test removal");
            removalResult = supplyService.removeSupply(testSupply);
        } catch (SQLException e) {
            fail("Error testing removeSupply: " + e.getMessage());
        }

        assertTrue("removeSupply() should return true when successful", removalResult);
    }
}
