package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SupplyLocationAllocationAccessTest {
    private DatabaseConnectionManager connectionManager;
    private SupplyLocationAllocationAccess supplyLocationAllocationDbAccess;
    private Connection connection;

    Supply placeholderSupply = new Blanket(-1);
    Location placeholderLocation = new Location(-1, "Test Location", "Test");

    @Before
    public void setUp() throws Exception {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        supplyLocationAllocationDbAccess = new SupplyLocationAllocationAccess();

        try {
            supplyLocationAllocationDbAccess.addEntry(placeholderSupply, placeholderLocation);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            supplyLocationAllocationDbAccess.removeEntry(placeholderSupply, placeholderLocation);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            supplyLocationAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", supplyLocationAllocationDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Map<Supply, Location>> allAllocations
                ;
        try {
            allAllocations = supplyLocationAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of supplies and their locations", allAllocations.isEmpty());
    }

    @Test
    public void testGetById() {
        Map<Supply, Location> allocation;

        try {
            allocation = supplyLocationAllocationDbAccess.getById(placeholderSupply, placeholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The retrieved location that a supply is located in should match the expected location when calling getById()",
                placeholderLocation, allocation.get(placeholderSupply));
    }

    @Test
    public void testAddEntry() {
        List<Map<Supply, Location>> allocationsBefore;
        List<Map<Supply, Location>> allocationsAfter;

        Supply newSupply = new Blanket(-2);
        Location newLocation = new Location(-2, "Test Location 2", "Test");

        try {
            allocationsBefore = supplyLocationAllocationDbAccess.getAll();
            supplyLocationAllocationDbAccess.addEntry(newSupply, newLocation);
            allocationsAfter = supplyLocationAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New supply-location entry should be added to the database",
                allocationsBefore.size(), allocationsAfter.size());

        try {
            supplyLocationAllocationDbAccess.removeEntry(newSupply, newLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while cleaning up addEntry test: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Map<Supply, Location>> allocationsBeforeRemoving;
        List<Map<Supply, Location>> allocationsAfterRemoving;

        Supply testSupply = new Blanket(-2);
        Location testLocation = new Location(-2, "Test Location 2", "Test");

        try {
            supplyLocationAllocationDbAccess.addEntry(testSupply, testLocation);
            allocationsBeforeRemoving = supplyLocationAllocationDbAccess.getAll();
            supplyLocationAllocationDbAccess.removeEntry(testSupply, testLocation);
            allocationsAfterRemoving = supplyLocationAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Unwanted supply-location entry should be removed from the database",
                allocationsBeforeRemoving.size(), allocationsAfterRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() {
        Map<Supply, Location> allocation;

        Supply supplyNotInDb = new Blanket(-999);
        Location locationNotInDb = new Location(-999, "Non-existent Location", "Test");

        try {
            allocation = supplyLocationAllocationDbAccess.getById(supplyNotInDb, locationNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNonExistent: " + e.getMessage());
        }

        assertNull("A null association map should be returned for a supply/location association that isn't in the database"
                , allocation);
    }

    @Test
    public void testAddEntryThatAlreadyExists() {
        List<Map<Supply, Location>> allocationsBefore;
        List<Map<Supply, Location>> allocationsAfter;

        try {
            allocationsBefore = supplyLocationAllocationDbAccess.getAll();
            supplyLocationAllocationDbAccess.addEntry(placeholderSupply, placeholderLocation);
            allocationsAfter = supplyLocationAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntryThatAlreadyExists: " + e.getMessage());
        }

        assertEquals("Adding an existing supply-location entry should not increase the total count",
                allocationsBefore.size(), allocationsAfter.size());
    }

    @Test
    public void testRemoveEntryNotInDb() {
        List<Map<Supply, Location>> allocationsBefore;
        List<Map<Supply, Location>> allocationsAfter;

        Supply supplyNotInDb = new Blanket(-999);
        Location locationNotInDb = new Location(-999, "Location not in db", "Test");

        try {
            allocationsBefore = supplyLocationAllocationDbAccess.getAll();
            supplyLocationAllocationDbAccess.removeEntry(supplyNotInDb, locationNotInDb);
            allocationsAfter = supplyLocationAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeNonExistentEntry: " + e.getMessage());
        }

        assertEquals("Removing an entry that isn't in the database should not affect the database",
                allocationsBefore.size(), allocationsAfter.size());
    }

    @Test
    public void testAddEntryWithNullArguments() {
        boolean success;

        try {
            success = supplyLocationAllocationDbAccess.addEntry(null, null);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeNonExistentEntry: " + e.getMessage());
        }

        assertFalse("addEntry() should return false when attempting to add a null entry", success);
    }

}