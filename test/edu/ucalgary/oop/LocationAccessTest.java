package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LocationAccessTest {
    private DatabaseConnectionManager connectionManager;
    private LocationAccess locationDbAccess;
    private Connection connection;

    Location placeholderLocation = new Location(-1, "Test Location", "test");

    @Before
    public void setUp() {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        locationDbAccess = new LocationAccess();

        locationDbAccess.addEntry(placeholderLocation);
    }

    @After
    public void tearDown() {
        locationDbAccess.removeEntry(placeholderLocation);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotNull("getQueryResults should retrieve a valid query", locationDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Location> retrievedLocations;
        try {
            retrievedLocations = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Locations", retrievedLocations.isEmpty());
    }

    @Test
    public void testGetById() {
        Location testLocation;
        try {
            testLocation = locationDbAccess.getById(0);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("The LOCATION_ID of testLocation should be equal to the id selected when calling getById()",
                testLocation.getLocationId(), 0);
    }

    @Test
    public void testUpdateInfo() {
        Location originalLocation;
        Location updatedLocation;

        try {
            originalLocation = locationDbAccess.getById(0);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationDbAccess.updateInfo("name", "updated name");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            updatedLocation = locationDbAccess.getById(0);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Location information should be updated as expected",
                originalLocation.getName(), updatedLocation.getName());
    }

    @Test
    public void testGetInfo() {
        Location testLocation;
        String retrievedName;

        try {
            testLocation = locationDbAccess.getById(0);
            retrievedName = locationDbAccess.getInfo("name");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("Retrieved info should match info in retrieved Location", retrievedName,
                testLocation.getName());
    }

    @Test
    public void testAddEntry() {
        List<Location> locationsBeforeAdding;
        List<Location> locationsAfterAdding;

        try {
            locationsBeforeAdding = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        Location newLocation = new Location(2, "New Test Location", "456 Test Ave");
        try {
            locationDbAccess.addEntry(newLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationsAfterAdding = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("New location should be added in the database", locationsAfterAdding.toArray().length,
                locationsBeforeAdding.toArray().length);
    }

    @Test
    public void testRemoveEntry() {
        List<Location> locationsBeforeRemoving;
        List<Location> locationsAfterRemoving;

        Location exLocation = new Location(2, "New Test Location", "ex location");

        try {
            locationDbAccess.addEntry(exLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationsBeforeRemoving = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationDbAccess.removeEntry(exLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationsAfterRemoving = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Unwanted location should be no longer be in the database",
                locationsAfterRemoving.toArray().length, locationsBeforeRemoving.toArray().length);
    }

    @Test
    public void testGetByNonExistentId() {
        Location testLocation;

        try {
            testLocation = locationDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNull("getById with non-existent ID should return null", testLocation);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = true;

        try {
            locationDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            success = false;
        }

        assertFalse("Trying to update a non-existent field should fail", success);
    }

    @Test
    public void testRemoveNonExistentEntry() {
        Location nonExistentLocation = new Location(-999, "Non-existent", "Nowhere St");
        boolean success = false;

        try {
            success = locationDbAccess.removeEntry(nonExistentLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("Removing non-existent entry should return false", success);
    }
}
