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
    public void testSetQueryResults() {
        assertNull("Before executing any queries, queryResults should be null", inquiryDbAccess.getQueryResults());

        try {
            inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotNull("setQueryResults should set a valid query to queryResults",
                inquiryDbAccess.getQueryResults());
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
    }

    @Test
    public void testRemoveEntry() {
    }
}
