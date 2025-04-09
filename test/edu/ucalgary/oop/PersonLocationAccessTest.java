package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class PersonLocationAccessTest {
    private PersonLocationAccess personLocationDbAccess;

    Person placeholderPerson = new Person(-1, "Test Person", "Male", "111-1111");
    Location placeholderLocation = new Location(-1, "Test Location", "Test");

    @Before
    public void setUp() throws Exception {
        personLocationDbAccess = new PersonLocationAccess();

        try {
            personLocationDbAccess.addEntry(placeholderPerson, placeholderLocation);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            personLocationDbAccess.removeEntry(placeholderPerson, placeholderLocation);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Test
    public void testGetQueryResults() {
        try {
            personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", personLocationDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Map<Person, Location>> allRetrievedLocationOccupants  = null;

        try {
            allRetrievedLocationOccupants = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of persons and their location", allRetrievedLocationOccupants.isEmpty());
    }

    @Test
    public void testGetById() {
        Map<Person, Location> retrievedLocationOccupant = null;

        try {
            retrievedLocationOccupant = personLocationDbAccess.getById(placeholderPerson, placeholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The retrieved person's location should match the expected location when calling getById()",
                retrievedLocationOccupant.get(placeholderPerson), placeholderLocation);
    }

    @Test
    public void testAddEntry() {
        List<Map<Person, Location>> locationOccupantsBeforeAdding = null;
        List<Map<Person, Location>> locationOccupantsAfterAdding = null;

        Person newPlaceholderPerson = new Person(-2, "Test Person 2", "Male", "222-2222");
        Location newPlaceholderLocation = new Location(-2, "Test Location 2", "Test");

        try {
            locationOccupantsBeforeAdding = personLocationDbAccess.getAll();
            personLocationDbAccess.addEntry(newPlaceholderPerson, newPlaceholderLocation);
            locationOccupantsAfterAdding = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New person-location entry should be added to the database", locationOccupantsAfterAdding.size(),
                locationOccupantsBeforeAdding.size());

        try {
             personLocationDbAccess.removeEntry(newPlaceholderPerson, newPlaceholderLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Map<Person, Location>> locationOccupantsBeforeRemoving = null;
        List<Map<Person, Location>> locationOccupantsAfterRemoving = null;

        Person exPlaceholderPerson = new Person(-2, "Test Person 2", "Male", "222-2222");
        Location exPlaceholderLocation = new Location(-2, "Test Location 2", "Test");

        try {
            personLocationDbAccess.addEntry(exPlaceholderPerson, exPlaceholderLocation);
            locationOccupantsBeforeRemoving = personLocationDbAccess.getAll();
            personLocationDbAccess.removeEntry(exPlaceholderPerson, exPlaceholderLocation);
            locationOccupantsAfterRemoving = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Unwanted person-location entry should be no longer be in the database",
                locationOccupantsAfterRemoving.size(), locationOccupantsBeforeRemoving.size());
    }

    @Test
    public void testGetOccupantsOfLocation() {
        Person[] testOccupants = null;

        Location testLocation = new Location(-2, "Test location", "Test");
        Person testPerson = new Person(-2, "Test Person 2", "Female", "222-2222");

        testLocation.addOccupant(testPerson);

        try {
            personLocationDbAccess.addEntry(testPerson, testLocation);
            testOccupants = personLocationDbAccess.getOccupantsOfLocation(testLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getOccupantsOfLocation: " + e.getMessage());
        }

        assertEquals("Retrieved Location occupants should match expected occupants", testLocation.getOccupants(),
                testOccupants);
    }

    @Test
    public void testGetByIdNotInDb() {
        Map<Person, Location> retrievedLocationOccupant = null;

        Person personNotInDb = new Person(-999, "Person not in db", "Male", "999-9999");
        Location locationNotInDb = new Location(-999, "Location not in db", "Test");

        try {
            retrievedLocationOccupant = personLocationDbAccess.getById(personNotInDb, locationNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing GetByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null association map should be returned for a non-existent person/location association",
                retrievedLocationOccupant);
    }

    @Test
    public void testAddEntryThatAlreadyExists() {
        List<Map<Person, Location>> locationOccupantsBeforeAdding = null;
        List<Map<Person, Location>> locationOccupantsAfterAdding = null;

        try {
            locationOccupantsBeforeAdding = personLocationDbAccess.getAll();
            personLocationDbAccess.addEntry(placeholderPerson, placeholderLocation);
            locationOccupantsAfterAdding = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntryThatAlreadyExists: " + e.getMessage());
        }

        assertEquals("Adding an existing person-location entry should not increase the total count",
                locationOccupantsBeforeAdding.size(), locationOccupantsAfterAdding.size());
    }

    @Test
    public void testRemoveEntryNotInDb() {
        List<Map<Person, Location>> locationOccupantsBeforeRemoving = null;
        List<Map<Person, Location>> locationOccupantsAfterRemoving = null;

        Person personNotInDb = new Person(-999, "Person not in db", "Male", "999-9999");
        Location locationNotInDb = new Location(-999, "Location not in db", "Test");

        try {
            locationOccupantsBeforeRemoving = personLocationDbAccess.getAll();
            personLocationDbAccess.removeEntry(personNotInDb, locationNotInDb);
            locationOccupantsAfterRemoving = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertEquals("Removing an entry that isn't in the database should not affect the database",
                locationOccupantsBeforeRemoving.size(), locationOccupantsAfterRemoving.size());
    }

    @Test
    public void testGetOccupantsOfLocationNoOccupants() {
        Person[] testOccupants = null;
        Location emptyLocation = new Location(-997, "Empty Location", "Test");

        try {
            testOccupants = personLocationDbAccess.getOccupantsOfLocation(emptyLocation);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getOccupantsOfLocationNoOccupants: " + e.getMessage());
        }

        assertNotNull("An empty occupant array should be returned if the location has no occupants",
                testOccupants);
        assertEquals("Empty location should have zero occupants", 0, testOccupants.length);
    }

    @Test
    public void testAddEntryWithNullArguments() {
        boolean success = true;

        try {
            success = personLocationDbAccess.addEntry(null, null);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntryWithNullArguments: " + e.getMessage());
        }

        assertFalse("addEntry() should return false when attempting to add a null entry",
                success);
    }
}
