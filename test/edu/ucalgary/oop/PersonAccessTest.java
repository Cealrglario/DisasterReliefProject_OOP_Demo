package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class PersonAccessTest {
    private PersonAccess<String> personDbAccess;

    Person placeholderPerson;

    @Before
    public void setUp() throws Exception {
        personDbAccess = new PersonAccess<>();

        try {
            placeholderPerson = personDbAccess.addPerson("Test person", "Female", null, "111-1111");
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            personDbAccess.removePerson(placeholderPerson);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Test
    public void testGetQueryResults() {
        try {
            personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", personDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Person> retrievedPersons = null;

        try {
            retrievedPersons = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Persons", retrievedPersons.isEmpty());
    }

    @Test
    public void testGetById() {
        Person testPerson = null;

        try {
            testPerson = personDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The ASSIGNED_ID of testPerson should match the id selected when calling getById()",
                1, testPerson.getAssignedId());
    }

    @Test
    public void testUpdateInfo() {
        Person updatedPerson = null;

        try {
            personDbAccess.updateInfo("first_name", "new name", 1);
            updatedPerson = personDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfo: " + e.getMessage());
        }

        assertEquals("Person first name should be updated as expected",
                "new name", updatedPerson.getFirstName());
    }

    @Test
    public void testGetInfo() {
        Person testPerson = null;
        String retrievedFirstName = null;

        try {
            testPerson = personDbAccess.getById(1);
            retrievedFirstName = personDbAccess.getInfo("first_name", 1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved first name should match first name in retrieved Person",
                retrievedFirstName, testPerson.getFirstName());
    }

    @Test
    public void testAddEntry() {
        List<Person> personsBeforeAdding = null;
        List<Person> personsAfterAdding = null;

        Person newPerson = null;

        try {
            personsBeforeAdding = personDbAccess.getAll();
            newPerson = personDbAccess.addPerson("Ex Test Person", "Non-binary", null, "333-3333");
            personsAfterAdding = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New person should be added in the database",
                personsAfterAdding.size(), personsBeforeAdding.size());

        try {
            personDbAccess.removePerson(newPerson);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Person> personsBeforeRemoving = null;
        List<Person> personsAfterRemoving = null;

        Person exPerson;

        try {
            exPerson = personDbAccess.addPerson("Ex Test Person", "Non-binary", null, "333-3333");
            personsBeforeRemoving = personDbAccess.getAll();
            personDbAccess.removePerson(exPerson);
            personsAfterRemoving = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Removed person should no longer be in the database",
                personsAfterRemoving.size(), personsBeforeRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() {
        Person testPerson = null;

        try {
            testPerson = personDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve a Person that isn't in the database",
                testPerson);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = true;

        try {
            success = personDbAccess.updateInfo("non_existent_field", "test value", 1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfoWithInvalidField: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success = true;
        Person personNotInDb = new Person(-999,  "Test Person", "Non-binary", "333-3333");

        try {
            success = personDbAccess.removePerson(personNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a Person that isn't in the database",
                success);
    }
}
