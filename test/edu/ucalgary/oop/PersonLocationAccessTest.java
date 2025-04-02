package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class PersonLocationAccessTest {
    private DatabaseConnectionManager connectionManager;
    private PersonLocationAccess personLocationDbAccess;
    private Connection connection;

    Person placeholderPerson = new Person(-1, "Test Person", "Male", "111-1111");
    Location placeholderLocation = new Location(-1, "Test Location", "Test");

    @Before
    public void setUp() {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        personLocationDbAccess = new PersonLocationAccess();

        personLocationDbAccess.addEntry(placeholderPerson, placeholderLocation);
    }

    @After
    public void tearDown() {
        personLocationDbAccess.removeEntry(placeholderPerson, placeholderLocation);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", personLocationDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Map<Person, Location>> allRetrievedLocationOccupants;

        try {
            allRetrievedLocationOccupants = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of persons and their location", allRetrievedLocationOccupants.isEmpty());
    }

    @Test
    public void testGetById() {
        Map<Person, Location> retrievedLocationOccupant;

        try {
            retrievedLocationOccupant = personLocationDbAccess.getById(placeholderPerson, placeholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("The retrieved person located within a location should match the expected person when calling getById()",
                retrievedLocationOccupant.get(placeholderLocation), placeholderPerson);
    }

    @Test
    public void testAddEntry() {
        List<Map<Person, Location>> locationOccupantsBeforeAdding;
        List<Map<Person, Location>> locationOccupantsAfterAdding;

        Person newPlaceholderPerson = new Person(-2, "Test Person 2", "Male", "222-2222");
        Location newPlaceholderLocation = new Location(-2, "Test Location 2", "Test");

        try {
            locationOccupantsBeforeAdding = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personLocationDbAccess.addEntry(newPlaceholderPerson, newPlaceholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationOccupantsAfterAdding = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("New person should be added to location in the database", locationOccupantsAfterAdding.size(),
                locationOccupantsBeforeAdding.size());

        try {
             personLocationDbAccess.removeEntry(newPlaceholderPerson, newPlaceholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Map<Person, Location>> locationOccupantsBeforeRemoving;
        List<Map<Person, Location>> locationOccupantsAfterRemoving;

        Person exPlaceholderPerson = new Person(-2, "Test Person 2", "Male", "222-2222");
        Location exPlaceholderLocation = new Location(-2, "Test Location 2", "Test");

        try {
            personLocationDbAccess.addEntry(exPlaceholderPerson, exPlaceholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationOccupantsBeforeRemoving = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personLocationDbAccess.removeEntry(exPlaceholderPerson, exPlaceholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            locationOccupantsAfterRemoving = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Unwanted inquiry should be no longer be in the database",
                locationOccupantsAfterRemoving.size(), locationOccupantsBeforeRemoving.size());
    }

    @Test
    public void testGetOccupantsOfLocation() {
        Person[] testOccupants;
        Location testLocation = new Location(-2, "Test location", "Test");
        Person testPerson = new Person(-2, "Test Person 2", "Female", "222-2222");

        testLocation.addOccupant(testPerson);

        try {
            personLocationDbAccess.addEntry(testPerson, testLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            testOccupants = personLocationDbAccess.getOccupantsOfLocation(testLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("Retrieved Location occupants should match expected occupants", testLocation.getOccupants(),
                testOccupants);
    }
}
