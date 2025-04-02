package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Stack;

public class SupplyAccessTest {
    private DatabaseConnectionManager connectionManager;
    private SupplyAccess supplyDbAccess;
    private Connection connection;

    Supply placeholderSupply = new Blanket(-1);

    @Before
    public void setUp() throws Exception {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        supplyDbAccess = new SupplyAccess();

        placeholderSupply.setComments("Test comment");

        supplyDbAccess.addEntry(placeholderSupply);
    }

    @After
    public void tearDown() {
        supplyDbAccess.removeEntry(placeholderSupply);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", supplyDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Supply> retrievedSupplies;

        try {
            retrievedSupplies = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Supplies", retrievedSupplies.isEmpty());
    }

    @Test
    public void testGetById() {
        Supply testSupply;

        try {
            testSupply = supplyDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("The SUPPLY_ID of testSupply should match the id selected when calling getById()",
                -1, testSupply.getSupplyId());
    }

    @Test
    public void testUpdateInfo() {
        Supply originalSupply;
        Supply updatedSupply;

        try {
            originalSupply = supplyDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            supplyDbAccess.updateInfo("comments", "new comments");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            updatedSupply = supplyDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Comment should be updated as expected",
                originalSupply.getComments(), updatedSupply.getComments());
    }

    @Test
    public void testGetInfo() {
        Supply testSupply;
        String retrievedType;

        try {
            testSupply = supplyDbAccess.getById(-1);
            retrievedType = supplyDbAccess.getInfo("type");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("Retrieved type should be Blanket",
                testSupply.getType(), retrievedType);
    }

    @Test
    public void testAddEntry() {
        List<Supply> suppliesBeforeAdding;
        List<Supply> suppliesAfterAdding;

        Supply newSupply = new Blanket(-2);
        newSupply.setComments("Test add");

        try {
            suppliesBeforeAdding = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            supplyDbAccess.addEntry(newSupply);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            suppliesAfterAdding = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("New supply should be added in the database",
                suppliesAfterAdding.size(), suppliesBeforeAdding.size());

        try {
            supplyDbAccess.removeEntry(newSupply);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Supply> suppliesBeforeRemoving;
        List<Supply> suppliesAfterRemoving;

        Supply exSupply = new Blanket(-2);

        try {
            supplyDbAccess.addEntry(exSupply);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            suppliesBeforeRemoving = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            supplyDbAccess.removeEntry(exSupply);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            suppliesAfterRemoving = supplyDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Removed supply should no longer be in the database",
                suppliesAfterRemoving.size(), suppliesBeforeRemoving.size());
    }

    @Test
    public void testGetByNonExistentId() {
        Supply testSupply;

        try {
            testSupply = supplyDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve a non-existent Supply",
                testSupply;
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success;

        try {
            success = supplyDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success;
        Blanket blanketNotInDb = new Blanket(-999);

        try {
            success = supplyDbAccess.removeEntry(blanketNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a Supply that isn't in the database",
                success);
    }
}
