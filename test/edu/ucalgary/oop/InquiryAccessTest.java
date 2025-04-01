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
    public void setUp() {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        inquiryDbAccess = new InquiryAccess();

        inquiryDbAccess.addEntry(placeholderInquiry);
    }

    @After
    public void tearDown() {
        inquiryDbAccess.removeEntry(placeholderInquiry);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotNull("getQueryResults should retrieve a valid query", inquiryDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Inquiry> retrievedInquiries;

        try {
            retrievedInquiries = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Inquiries", retrievedInquiries.isEmpty());
    }

    @Test
    public void testGetById() {
        Inquiry testInquiry;

        try {
            testInquiry = inquiryDbAccess.getById(0);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
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
         } catch (SQLException e) {
             fail("SQLException occurred while testing: " + e.getMessage());
         }

        try {
            inquiryDbAccess.updateInfo("comments", "updated comment");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            updatedInquiry = inquiryDbAccess.getById(0);
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
            testInquiry = inquiryDbAccess.getById(0);
            retrievedComments = inquiryDbAccess.getInfo("comments");
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
            inquiriesBeforeAdding = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        Inquiry newInquiry = new Inquiry(2, 1, LocalDate.now(), "new inquiry");

        try {
            inquiryDbAccess.addEntry(newInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            inquiriesAfterAdding = inquiryDbAccess.getAll();
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
            inquiryDbAccess.addEntry(exInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            inquiriesBeforeRemoving = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            inquiryDbAccess.removeEntry(exInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            inquiriesAfterRemoving = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Unwanted inquiry should be no longer be in the database",
                inquiriesAfterRemoving.toArray().length, inquiriesBeforeRemoving.toArray().length);
    }

    @Test
    public void testGetByNonExistentId() {
        Inquiry testInquiry;
        try {
            testInquiry = inquiryDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNull("getById with non-existent ID should return null", testInquiry);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = true;

        try {
            inquiryDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            success = false;
        }

        assertFalse("Updating with invalid field name should fail", success);
    }

    @Test
    public void testRemoveNonExistentEntry() {
        Inquiry nonExistentInquiry = new Inquiry(-999, 1, LocalDate.now(), "doesn't exist");
        boolean success = false;

        try {
            success = inquiryDbAccess.removeEntry(nonExistentInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("Removing non-existent entry should return false", success);
    }
}
