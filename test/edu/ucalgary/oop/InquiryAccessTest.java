package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InquiryAccessTest {
    private InquiryAccess<Object> inquiryDbAccess;
    private Inquiry placeholderInquiry = null;

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
        assertNotNull("getQueryResults() should retrieve a valid query result set", inquiryDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Inquiry> retrievedInquiries = null;
        try {
            retrievedInquiries = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }
        assertFalse("getAll() should return a non-empty list of Inquiries", retrievedInquiries.isEmpty());
    }

    @Test
    public void testGetById() {
        Inquiry testInquiry = null;
        try {
            testInquiry = inquiryDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }
        assertEquals("The inquiry ID should match the ID used in getById()", 1, testInquiry.getInquiryId());
    }

    @Test
    public void testUpdateInfo() {
        Inquiry originalInquiry = null;
        try {
            originalInquiry = inquiryDbAccess.getById(placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while retrieving original inquiry: " + e.getMessage());
        }

        String originalComment = originalInquiry.getInfoProvided();
        String updatedComment = "updated comment";

        boolean updateSuccess = false;
        try {
            updateSuccess = inquiryDbAccess.updateInfo("comments", updatedComment, placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while updating inquiry: " + e.getMessage());
        }

        assertTrue("updateInfo() should return true on successful update", updateSuccess);

        Inquiry updatedInquiry = null;
        try {
            updatedInquiry = inquiryDbAccess.getById(placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while retrieving updated inquiry: " + e.getMessage());
        }

        assertNotEquals("Inquiry comment should be updated as expected", originalComment, updatedInquiry.getInfoProvided());
    }

    @Test
    public void testGetInfo() {
        Inquiry testInquiry = null;
        try {
            testInquiry = inquiryDbAccess.getById(placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while retrieving inquiry: " + e.getMessage());
        }

        String retrievedComments = null;
        try {
            retrievedComments = (String) inquiryDbAccess.getInfo("comments", placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while getting info: " + e.getMessage());
        }

        assertEquals("Retrieved comments should match the inquiry's info", testInquiry.getInfoProvided(), retrievedComments);
    }

    @Test
    public void testAddInquiry() {
        List<Inquiry> inquiriesBeforeAdding = null;
        try {
            inquiriesBeforeAdding = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while getting inquiries before adding: " + e.getMessage());
        }

        Inquiry newInquiry = null;
        try {
            newInquiry = inquiryDbAccess.addInquiry(2, 2, LocalDate.now(), "new inquiry");
        } catch (SQLException e) {
            fail("SQLException occurred while adding inquiry: " + e.getMessage());
        }

        assertNotNull("addInquiry() should return a non-null Inquiry object", newInquiry);

        List<Inquiry> inquiriesAfterAdding = null;
        try {
            inquiriesAfterAdding = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while getting inquiries after adding: " + e.getMessage());
        }

        assertTrue("Number of inquiries should increase after adding",
                inquiriesAfterAdding.size() > inquiriesBeforeAdding.size());

        try {
            inquiryDbAccess.removeInquiry(newInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while removing test inquiry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveInquiry() {
        Inquiry tempInquiry = null;
        try {
            tempInquiry = inquiryDbAccess.addInquiry(2, 2, LocalDate.now(), "temporary inquiry");
        } catch (SQLException e) {
            fail("SQLException occurred while adding temporary inquiry: " + e.getMessage());
        }

        List<Inquiry> inquiriesBeforeRemoving = null;
        try {
            inquiriesBeforeRemoving = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while getting inquiries before removing: " + e.getMessage());
        }

        boolean removeSuccess = false;
        try {
            removeSuccess = inquiryDbAccess.removeInquiry(tempInquiry);
        } catch (SQLException e) {
            fail("SQLException occurred while removing inquiry: " + e.getMessage());
        }

        assertTrue("removeInquiry() should return true on successful removal", removeSuccess);

        List<Inquiry> inquiriesAfterRemoving = null;
        try {
            inquiriesAfterRemoving = inquiryDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while getting inquiries after removing: " + e.getMessage());
        }

        assertTrue("Number of inquiries should decrease after removal",
                inquiriesAfterRemoving.size() < inquiriesBeforeRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() {
        Inquiry testInquiry = null;
        try {
            testInquiry = inquiryDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById for non-existent ID: " + e.getMessage());
        }

        assertNull("getById() should return null for a non-existent inquiry ID", testInquiry);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = false;
        try {
            success = inquiryDbAccess.updateInfo("non_existent_field", "test value", placeholderInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfo with invalid field: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when updating a non-existent field", success);
    }

    @Test
    public void testRemoveInquiryNotInDb() {
        Inquiry inquiryNotInDb = new Inquiry(-9999, 2, 2, "non-existent inquiry");
        boolean success = false;
        try {
            success = inquiryDbAccess.removeInquiry(inquiryNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeInquiry for non-existent inquiry: " + e.getMessage());
        }

        assertFalse("removeInquiry() should return false for a non-existent inquiry", success);
    }
}
