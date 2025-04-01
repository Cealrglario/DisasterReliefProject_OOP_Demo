package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
    public void testUpdateInfo() {
        Inquiry originalInquiry;
        Inquiry updatedInquiry;

        try {
            originalInquiry = personLocationDbAccess.getById(0);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personLocationDbAccess.updateInfo("comments", "updated comment");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            updatedInquiry = personLocationDbAccess.getById(0);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Inquiry information should be updated as expected",
                originalInquiry.getInfoProvided(), updatedInquiry.getInfoProvided());
    }

    @Test
    public void testGetInfo() {
        Inquiry testInquiry;
        String retrievedComments;

        try {
            testInquiry = personLocationDbAccess.getById(0);
            retrievedComments = personLocationDbAccess.getInfo("comments");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("Retrieved info should match info in retrieved Inquiry", retrievedComments,
                testInquiry.getInfoProvided());
    }

    @Test
    public void testAddEntry() {
        List<Inquiry> inquiriesBeforeAdding;
        List<Inquiry> inquiriesAfterAdding;

        try {
            inquiriesBeforeAdding = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        Inquiry newInquiry = new Inquiry(2, 1, LocalDate.now(), "new inquiry");

        try {
            personLocationDbAccess.addEntry(newInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            inquiriesAfterAdding = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("New inquiry should be added in the database", inquiriesAfterAdding.toArray().length,
                inquiriesBeforeAdding.toArray().length);
    }

    @Test
    public void testRemoveEntry() {
        List<Inquiry> inquiriesBeforeRemoving;
        List<Inquiry> inquiriesAfterRemoving;

        Inquiry exInquiry = new Inquiry(2, 1, LocalDate.now(), "new inquiry");

        try {
            personLocationDbAccess.addEntry(exInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            inquiriesBeforeRemoving = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            personLocationDbAccess.removeEntry(exInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            inquiriesAfterRemoving = personLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Unwanted inquiry should be no longer be in the database",
                inquiriesAfterRemoving.size(), inquiriesBeforeRemoving.size());
    }

    @Test
    public void testGetOccupantsOfLocation() {
    }
}
