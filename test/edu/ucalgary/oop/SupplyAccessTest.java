package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class SupplyAccessTest {
    private SupplyAccess<String> supplyDbAccess;

    Supply placeholderSupply;

    @Before
    public void setUp() throws Exception {
        supplyDbAccess = new SupplyAccess<>();

        try {
            placeholderSupply = supplyDbAccess.addSupply("Blanket", null);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            supplyDbAccess.removeSupply(placeholderSupply);
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
            testSupply = supplyDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The SUPPLY_ID of testSupply should match the id selected when calling getById()",
                1, testSupply.getSupplyId());
    }

    @Test
    public void testUpdateInfo() {
        Supply updatedSupply = null;

        try {
            supplyDbAccess.updateInfo("comments", "new comments", 1);
            updatedSupply = supplyDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfo: " + e.getMessage());
        }

        assertEquals("Comment should be updated as expected",
                "new comments", updatedSupply.getComments());
    }

    @Test
    public void testGetInfo() {
        Supply testSupply = null;
        String retrievedType = null;

        try {
            testSupply = supplyDbAccess.getById(1);
            retrievedType = supplyDbAccess.getInfo("type", 1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved type should match actual type",
                testSupply.getType().toLowerCase(), retrievedType);
    }

    @Test
    public void testAddEntry() {
        List<Supply> suppliesBeforeAdding = null;
        List<Supply> suppliesAfterAdding = null;

        Supply newSupply = null;

        try {
            suppliesBeforeAdding = supplyDbAccess.getAll();
            newSupply = supplyDbAccess.addSupply("Blanket", null);
            suppliesAfterAdding = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertTrue("New supply should be added in the database",
                suppliesAfterAdding.size() > suppliesBeforeAdding.size());

        try {
            supplyDbAccess.removeSupply(newSupply);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Supply> suppliesBeforeRemoving = null;
        List<Supply> suppliesAfterRemoving = null;

        Supply exSupply = null;

        try {
            exSupply = supplyDbAccess.addSupply("Blanket", null);
            suppliesBeforeRemoving = supplyDbAccess.getAll();
            supplyDbAccess.removeSupply(exSupply);
            suppliesAfterRemoving = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertTrue("Removed supply should no longer be in the database",
                suppliesAfterRemoving.size() < suppliesBeforeRemoving.size());
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
            success = supplyDbAccess.updateInfo("non_existent_field", "test value", 1);
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
            success = supplyDbAccess.removeSupply(blanketNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a Supply that isn't in the database",
                success);
    }
}
