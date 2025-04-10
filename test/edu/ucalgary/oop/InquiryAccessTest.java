package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InquiryAccessTest {
    private InquiryAccess<String> inquiryDbAccess;

    Inquiry placeholderInquiry = null;

    @Before
    public void setUp() throws Exception {
        inquiryDbAccess = new InquiryAccess<>();

        try {
            placeholderInquiry = inquiryDbAccess.addInquiry(1, 1, LocalDate.now(), "placeholder");
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            inquiryDbAccess.removeInquiry(placeholderInquiry);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
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
        List<Inquiry> retrievedInquiries = null;

        try {
            retrievedInquiries = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of Inquiries", retrievedInquiries.isEmpty());
    }

    @Test
    public void testGetById() {
        Inquiry testInquiry = null;

        try {
            testInquiry = inquiryDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The INQUIRY_ID of testInquiryId should be equal to the id selected when calling getById()", 1,
                testInquiry.getInquiryId());
    }

    @Test
    public void testUpdateInfo() {
        Inquiry originalInquiry = null;
        Inquiry updatedInquiry = null;

         try {
             originalInquiry = inquiryDbAccess.getById(placeholderInquiry.getInquiryId());
             inquiryDbAccess.updateInfo("comments", "updated comment", placeholderInquiry.getInquiryId());
             updatedInquiry = inquiryDbAccess.getById(placeholderInquiry.getInquiryId());
         } catch (SQLException e) {
             fail("SQLException occurred while testing updateInfo: " + e.getMessage());
         }

        assertNotEquals("Inquiry information should be updated as expected",
                originalInquiry.getInfoProvided(), updatedInquiry.getInfoProvided());
    }

    @Test
    public void testGetInfo() {
        Inquiry testInquiry = null;
        String retrievedComments = null;

        try {
            testInquiry = inquiryDbAccess.getById(placeholderInquiry.getInquiryId());
            retrievedComments = inquiryDbAccess.getInfo("comments", placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved info should match info in retrieved Inquiry", retrievedComments,
                testInquiry.getInfoProvided());
    }

    @Test
    public void testAddInquiry() {
        List<Inquiry> inquiriesBeforeAdding = null;
        List<Inquiry> inquiriesAfterAdding = null;
        Inquiry newInquiry = null;

        try {
            inquiriesBeforeAdding = inquiryDbAccess.getAll();
            newInquiry = inquiryDbAccess.addInquiry(2, 2, LocalDate.now(), "new inquiry");
            inquiriesAfterAdding = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New Inquiry should be added in the database", inquiriesAfterAdding.size(),
                inquiriesBeforeAdding.size());

        try {
            inquiryDbAccess.removeInquiry(newInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Inquiry> inquiriesBeforeRemoving = null;
        List<Inquiry> inquiriesAfterRemoving = null;

        try {
            Inquiry exInquiry = inquiryDbAccess.addInquiry(2,2, LocalDate.now(), "new inquiry");
            inquiriesBeforeRemoving = inquiryDbAccess.getAll();
            inquiryDbAccess.removeInquiry(exInquiry);
            inquiriesAfterRemoving = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Unwanted Inquiry should be no longer be in the database",
                inquiriesAfterRemoving.size(), inquiriesBeforeRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() throws SQLException {
        Inquiry testInquiry = null;

        try {
            testInquiry = inquiryDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNotInDb: " + e.getMessage());
        }

        assertNull("testInquiry should be null as it tries to retrieve a non-existent entry", testInquiry);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = true;

        try {
            success = inquiryDbAccess.updateInfo("non_existent_field", "test value", placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfoWithInvalidField: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success = true;
        Inquiry inquiryNotInDb = new Inquiry(-9999, 2, 2, LocalDate.now(), "new inquiry");

        try {
            success = inquiryDbAccess.removeInquiry(inquiryNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove an Inquiry that isn't in the database",
                success);
    }
}
