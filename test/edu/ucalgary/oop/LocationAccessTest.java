package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class LocationAccessTest {
    private LocationAccess<String> locationDbAccess;

    Location placeholderLocation = new Location(-1, "Test Location", "Placeholder");

    @Before
    public void setUp() throws Exception {
        locationDbAccess = new LocationAccess<>();

        try {
            locationDbAccess.addEntry(placeholderLocation);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            locationDbAccess.removeEntry(placeholderLocation);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Test
    public void testGetQueryResults() {
        try {
            locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", locationDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Location> retrievedLocations = null;

        try {
            retrievedLocations = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Locations", retrievedLocations.isEmpty());
    }

    @Test
    public void testGetById() {
        Location testLocation = null;

        try {
            testLocation = locationDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The LOCATION_ID of testLocation should be equal to the id selected when calling getById()",
                testLocation.getLocationId(), -1);
    }

    @Test
    public void testUpdateInfo() {
        Location originalLocation = null;
        Location updatedLocation = null;

        try {
            originalLocation = locationDbAccess.getById(-1);
            locationDbAccess.updateInfo("name", "updated name");
            updatedLocation = locationDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfo: " + e.getMessage());
        }

        assertNotEquals("Location information should be updated as expected",
                originalLocation.getName(), updatedLocation.getName());
    }

    @Test
    public void testGetInfo() {
        Location testLocation = null;
        String retrievedName = null;

        try {
            testLocation = locationDbAccess.getById(-1);
            retrievedName = locationDbAccess.getInfo("name", -1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved info should match info in retrieved Location", retrievedName,
                testLocation.getName());
    }

    @Test
    public void testAddEntry() {
        List<Location> locationsBeforeAdding = null;
        List<Location> locationsAfterAdding = null;

        Location newLocation = new Location(-2, "New Test Location", "test add");

        try {
            locationsBeforeAdding = locationDbAccess.getAll();
            locationDbAccess.addEntry(newLocation);
            locationsAfterAdding = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New Location should be added in the database", locationsAfterAdding.toArray().length,
                locationsBeforeAdding.toArray().length);

        try {
            locationDbAccess.removeEntry(newLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Location> locationsBeforeRemoving = null;
        List<Location> locationsAfterRemoving = null;

        Location exLocation = new Location(-2, "New Test Location", "Test remove");

        try {
            locationDbAccess.addEntry(exLocation);
            locationsBeforeRemoving = locationDbAccess.getAll();
            locationDbAccess.removeEntry(exLocation);
            locationsAfterRemoving = locationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Unwanted Location should be no longer be in the database",
                locationsAfterRemoving.size(), locationsBeforeRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() {
        Location testLocation = null;

        try {
            testLocation = locationDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve a Location that isn't in the database",
                testLocation);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = true;

        try {
            success = locationDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfoWithInvalidField: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success = true;
        Location locationNotInDb = new Location(-2, "New Test Location", "Test not in db");

        try {
            success = locationDbAccess.removeEntry(locationNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a Location that isn't in the database",
                success);
    }
}
