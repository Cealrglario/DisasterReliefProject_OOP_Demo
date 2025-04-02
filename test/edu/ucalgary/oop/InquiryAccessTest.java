package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InquiryAccessTest {
    private DatabaseConnectionManager connectionManager;
    private InquiryAccess inquiryDbAccess;
    private Connection connection;

    Inquiry placeholderInquiry = new Inquiry(-1, -1, LocalDate.now(), "placeholder");

    @Before
    public void setUp() throws Exception {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        inquiryDbAccess = new InquiryAccess();

        try {
            inquiryDbAccess.addEntry(placeholderInquiry);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            inquiryDbAccess.removeEntry(placeholderInquiry);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", inquiryDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Inquiry> retrievedInquiries;

        try {
            retrievedInquiries = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Inquiries", retrievedInquiries.isEmpty());
    }

    @Test
    public void testGetById() {
        Inquiry testInquiry;

        try {
            testInquiry = inquiryDbAccess.getById(0);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The INQUIRY_ID of testInquiryId should be equal to the id selected when calling getById()",
                testInquiry.getInquiryId(), 0);
    }

    @Test
    public void testUpdateInfo() {
        Inquiry originalInquiry;
        Inquiry updatedInquiry;

         try {
             originalInquiry = inquiryDbAccess.getById(0);
             inquiryDbAccess.updateInfo("comments", "updated comment");
             updatedInquiry = inquiryDbAccess.getById(0);
         } catch (SQLException e) {
             fail("SQLException occurred while testing updateInfo: " + e.getMessage());
         }

        assertNotEquals("Inquiry information should be updated as expected",
                originalInquiry.getInfoProvided(), updatedInquiry.getInfoProvided());
    }

    @Test
    public void testGetInfo() {
        Inquiry testInquiry;
        String retrievedComments;

        try {
            testInquiry = inquiryDbAccess.getById(0);
            retrievedComments = inquiryDbAccess.getInfo("comments");
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved info should match info in retrieved Inquiry", retrievedComments,
                testInquiry.getInfoProvided());
    }

    @Test
    public void testAddEntry() {
        List<Inquiry> inquiriesBeforeAdding;
        List<Inquiry> inquiriesAfterAdding;

        Inquiry newInquiry = new Inquiry(2, 1, LocalDate.now(), "new inquiry");

        try {
            inquiriesBeforeAdding = inquiryDbAccess.getAll();
            inquiryDbAccess.addEntry(newInquiry);
            inquiriesAfterAdding = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New Inquiry should be added in the database", inquiriesAfterAdding.size(),
                inquiriesBeforeAdding.size());

        try {
            inquiryDbAccess.removeEntry(newInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Inquiry> inquiriesBeforeRemoving;
        List<Inquiry> inquiriesAfterRemoving;

        Inquiry exInquiry = new Inquiry(2, 1, LocalDate.now(), "new inquiry");

        try {
            inquiryDbAccess.addEntry(exInquiry);
            inquiriesBeforeRemoving = inquiryDbAccess.getAll();
            inquiryDbAccess.removeEntry(exInquiry);
            inquiriesAfterRemoving = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Unwanted Inquiry should be no longer be in the database",
                inquiriesAfterRemoving.size(), inquiriesBeforeRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() {
        Inquiry testInquiry;

        try {
            testInquiry = inquiryDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve an Inquiry that isn't in the database",
                testInquiry);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success;

        try {
            success = inquiryDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfoWithInvalidField: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success;
        Inquiry inquiryNotInDb = new Inquiry(2, 1, LocalDate.now(), "new inquiry");

        try {
            success = inquiryDbAccess.removeEntry(inquiryNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove an Inquiry that isn't in the database",
                success);
    }
}
