package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LocationAccessTest {
    private DatabaseConnectionManager connectionManager;
    private LocationAccess locationDbAccess;
    private Connection connection;

    Location placeholderLocation = new Location(-1, "Test Location", "Placeholder");

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

        assertNotNull("getQueryResults() should retrieve a valid query", locationDbAccess.getQueryResults());
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
            testLocation = locationDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("The LOCATION_ID of testLocation should be equal to the id selected when calling getById()",
                testLocation.getLocationId(), -1);
    }

    @Test
    public void testUpdateInfo() {
        Location originalLocation;
        Location updatedLocation;

        try {
            originalLocation = locationDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationDbAccess.updateInfo("name", "updated name");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            updatedLocation = locationDbAccess.getById(-1);
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
            testLocation = locationDbAccess.getById(-1);
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

        Location newLocation = new Location(-2, "New Test Location", "test add");

        try {
            locationsBeforeAdding = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

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

        try {
            locationDbAccess.removeEntry(newLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Location> locationsBeforeRemoving;
        List<Location> locationsAfterRemoving;

        Location exLocation = new Location(-2, "New Test Location", "Test remove");

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
                locationsAfterRemoving.size(), locationsBeforeRemoving.size());
    }

    @Test (expected = SQLException.class)
    public void testGetByNonExistentId() throws SQLException {
        try {
            locationDbAccess.getById(-999);
        } catch (SQLException e) {
            throw new SQLException("SQLException occurred while testing: " + e.getMessage());
        }
    }

    @Test (expected = SQLException.class)
    public void testUpdateInfoWithInvalidField() throws SQLException {
        try {
            locationDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            throw new SQLException("SQLException occurred while testing: " + e.getMessage());
        }
    }

    @Test (expected = SQLException.class)
    public void testRemoveEntryNotInDb() throws SQLException {
        Location locationNotInDb = new Location(-999, "not in db test", "doesn't exist");

        try {
            locationDbAccess.removeEntry(locationNotInDb);
        } catch (SQLException e) {
            throw new SQLException("SQLException occurred while testing: " + e.getMessage());
        }
    }
}
