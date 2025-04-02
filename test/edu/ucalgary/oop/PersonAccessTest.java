package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PersonAccessTest {
    private DatabaseConnectionManager connectionManager;
    private PersonAccess personDbAccess;
    private Connection connection;

    Person placeholderPerson = new Person(-1, "Test Person", "Female", "111-1111");

    @Before
    public void setUp() throws Exception {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        personDbAccess = new PersonAccess();

        personDbAccess.addEntry(placeholderPerson);
    }

    @After
    public void tearDown() {
        personDbAccess.removeEntry(placeholderPerson);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", personDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Person> retrievedPersons;

        try {
            retrievedPersons = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Persons", retrievedPersons.isEmpty());
    }

    @Test
    public void testGetById() {
        Person testPerson;

        try {
            testPerson = personDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("The ASSIGNED_ID of testPerson should match the id selected when calling getById()",
                -1, testPerson.getAssignedId());
    }

    @Test
    public void testUpdateInfo() {
        Person originalPerson;
        Person updatedPerson;

        try {
            originalPerson = personDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personDbAccess.updateInfo("first_name", "new name");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            updatedPerson = personDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Person first name should be updated as expected",
                originalPerson.getFirstName(), updatedPerson.getFirstName());
    }

    @Test
    public void testGetInfo() {
        Person testPerson;
        String retrievedFirstName;

        try {
            testPerson = personDbAccess.getById(-1);
            retrievedFirstName = personDbAccess.getInfo("first_name");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("Retrieved first name should match first name in retrieved Person",
                retrievedFirstName, testPerson.getFirstName());
    }

    @Test
    public void testAddEntry() {
        List<Person> personsBeforeAdding;
        List<Person> personsAfterAdding;

        Person newPerson = new Person(-2, "New Test Person", "Male", "222-2222");

        try {
            personsBeforeAdding = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personDbAccess.addEntry(newPerson);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personsAfterAdding = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("New person should be added in the database",
                personsAfterAdding.size(), personsBeforeAdding.size());

        try {
            personDbAccess.removeEntry(newPerson);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Person> personsBeforeRemoving;
        List<Person> personsAfterRemoving;

        Person exPerson = new Person(-2,  "Ex Test Person", "Non-binary", "333-3333");

        try {
            personDbAccess.addEntry(exPerson);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personsBeforeRemoving = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personDbAccess.removeEntry(exPerson);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personsAfterRemoving = personDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Removed person should no longer be in the database",
                personsAfterRemoving.size(), personsBeforeRemoving.size());
    }

    @Test
    public void testGetByNonExistentId() {
        Person testPerson;

        try {
            testPerson = personDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve a non-existent Person",
                testPerson);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success;

        try {
            success = personDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success;
        Person personNotInDb = new Person(-2,  "Test Person", "Non-binary", "333-3333");

        try {
            success = personDbAccess.removeEntry(personNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a Person that isn't in the database",
                success);
    }
}
