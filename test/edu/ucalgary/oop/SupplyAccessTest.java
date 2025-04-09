package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class SupplyAccessTest {
    private SupplyAccess<String> supplyDbAccess;

    Supply placeholderSupply = new Blanket(-1);

    @Before
    public void setUp() throws Exception {
        supplyDbAccess = new SupplyAccess<>();

        placeholderSupply.setComments("Test comment");

        try {
            supplyDbAccess.addEntry(placeholderSupply);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            supplyDbAccess.removeEntry(placeholderSupply);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Test
    public void testGetQueryResults() {
        try {
            supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", supplyDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Supply> retrievedSupplies = null;

        try {
            retrievedSupplies = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Supplies", retrievedSupplies.isEmpty());
    }

    @Test
    public void testGetById() {
        Supply testSupply = null;

        try {
            testSupply = supplyDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The SUPPLY_ID of testSupply should match the id selected when calling getById()",
                -1, testSupply.getSupplyId());
    }

    @Test
    public void testUpdateInfo() {
        Supply originalSupply = null;
        Supply updatedSupply = null;

        try {
            originalSupply = supplyDbAccess.getById(-1);
            supplyDbAccess.updateInfo("comments", "new comments");
            updatedSupply = supplyDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfo: " + e.getMessage());
        }

        assertNotEquals("Comment should be updated as expected",
                originalSupply.getComments(), updatedSupply.getComments());
    }

    @Test
    public void testGetInfo() {
        Supply testSupply = null;
        String retrievedType = null;

        try {
            testSupply = supplyDbAccess.getById(-1);
            retrievedType = supplyDbAccess.getInfo("type", -1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved type should be Blanket",
                testSupply.getType(), retrievedType);
    }

    @Test
    public void testAddEntry() {
        List<Supply> suppliesBeforeAdding = null;
        List<Supply> suppliesAfterAdding = null;

        Supply newSupply = new Blanket(-2);
        newSupply.setComments("Test add");

        try {
            suppliesBeforeAdding = supplyDbAccess.getAll();
            supplyDbAccess.addEntry(newSupply);
            suppliesAfterAdding = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New supply should be added in the database",
                suppliesAfterAdding.size(), suppliesBeforeAdding.size());

        try {
            supplyDbAccess.removeEntry(newSupply);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Supply> suppliesBeforeRemoving = null;
        List<Supply> suppliesAfterRemoving = null;

        Supply exSupply = new Blanket(-2);

        try {
            supplyDbAccess.addEntry(exSupply);
            suppliesBeforeRemoving = supplyDbAccess.getAll();
            supplyDbAccess.removeEntry(exSupply);
            suppliesAfterRemoving = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Removed supply should no longer be in the database",
                suppliesAfterRemoving.size(), suppliesBeforeRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() {
        Supply testSupply = null;

        try {
            testSupply = supplyDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve a Supply that isn't in the database",
                testSupply);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = true;

        try {
            success = supplyDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfoWithInvalidField: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success = true;
        Supply blanketNotInDb = new Blanket(-999);

        try {
            success = supplyDbAccess.removeEntry(blanketNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a Supply that isn't in the database",
                success);
    }
}
