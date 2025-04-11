package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class LocationAccessTest {
    private LocationAccess<String> locationDbAccess;

    @Before
    public void setUp() throws Exception {
        locationDbAccess = new LocationAccess<>();
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
            testLocation = locationDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The LOCATION_ID of testLocation should be equal to the id selected when calling getById()",
                testLocation.getLocationId(), 1);
    }

    @Test
    public void testUpdateInfo() {
        Location originalLocation = null;
        Location updatedLocation = null;

        try {
            originalLocation = locationDbAccess.getById(1);
            locationDbAccess.updateInfo("name", "updated name", 1);
            updatedLocation = locationDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfo: " + e.getMessage());
        }

        assertEquals("Location information should be updated as expected",
                "updated name", updatedLocation.getName());
    }

    @Test
    public void testGetInfo() {
        Location testLocation = null;
        String retrievedName = null;

        try {
            testLocation = locationDbAccess.getById(1);
            retrievedName = locationDbAccess.getInfo("name", 1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved info should match info in retrieved Location", retrievedName,
                testLocation.getName());
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
            success = locationDbAccess.updateInfo("non_existent_field", "test value", 1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfoWithInvalidField: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }
}
