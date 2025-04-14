package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InquiryServiceTest {
    private InquiryService inquiryService;

    @Before
    public void setUp() {
        inquiryService = InquiryService.INSTANCE;
    }

    @Test
    public void testGetInquiryById() {
        Inquiry testInquiry = null;

        try {
            testInquiry = inquiryService.getInquiryById(1);
        } catch (SQLException e) {
            fail("Error testing getInquiryById: " + e.getMessage());
        }

        assertNotNull("getInquiryById() should return a non-null Inquiry object", testInquiry);
        assertEquals("getInquiryById() should retrieve the correct Inquiry", 1, testInquiry.getInquiryId());
    }

    @Test
    public void testGetAllInquiries() {
        List<Inquiry> retrievedInquiries = null;

        try {
            retrievedInquiries = inquiryService.getAllInquiries();
        } catch (SQLException e) {
            fail("Error testing getAllInquiries: " + e.getMessage());
        }

        assertNotNull("getAllInquiries() should retrieve a non-null list of Inquiries", retrievedInquiries);
    }

    @Test
    public void testAddInquiry() {
        Inquiry testInquiry = null;
        int testInquirerId = 1;
        int testMissingPersonId = 2;
        LocalDate testDate = LocalDate.now();
        String testInfo = "Test inquiry information";

        try {
            testInquiry = inquiryService.addInquiry(testInquirerId, testMissingPersonId, testDate, testInfo);
        } catch (SQLException e) {
            fail("Error testing addInquiry: " + e.getMessage());
        }

        assertNotNull("addInquiry() should create and return a valid Inquiry", testInquiry);
        assertEquals("addInquiry() should set the inquirerId correctly", testInquirerId, testInquiry.getInquirerid());
        assertEquals("addInquiry() should set the missingPersonId correctly", testMissingPersonId, testInquiry.getMissingPersonId());
        assertEquals("addInquiry() should set the infoProvided correctly", testInfo, testInquiry.getInfoProvided());

        try {
            inquiryService.removeInquiry(testInquiry);
        } catch (SQLException e) {
            fail("Error cleaning up after test: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateInquiryInfo() {
        Inquiry testInquiry = null;
        String newInfo = "Updated inquiry information";

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), "Test inquiry");
            boolean updateResult = inquiryService.updateInquiryInfo(testInquiry, newInfo);

            assertTrue("updateInquiryInfo() should return true for successful update", updateResult);
            assertEquals("updateInquiryInfo() should update the info in the Inquiry object",
                    newInfo, testInquiry.getInfoProvided());

            String retrievedInfo = (String) inquiryService.getInquiryInfo("comments", testInquiry.getInquiryId());
            assertEquals("updateInquiryInfo() should update the info in the database",
                    newInfo, retrievedInfo);
        } catch (SQLException e) {
            fail("Error testing updateInquiryInfo: " + e.getMessage());
        } finally {
            try {
                if (testInquiry != null) {
                    inquiryService.removeInquiry(testInquiry);
                }
            } catch (SQLException e) {
                fail("Error cleaning up after test: " + e.getMessage());
            }
        }
    }

    @Test
    public void testGetInquiryInfo() {
        Inquiry testInquiry = null;
        String testInfo = "Test inquiry for info retrieval";

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), testInfo);

            String retrievedInfo = (String) inquiryService.getInquiryInfo("comments", testInquiry.getInquiryId());
            assertEquals("getInquiryInfo() should retrieve the correct information",
                    testInfo, retrievedInfo);

            Integer retrievedInquirerId = (Integer) inquiryService.getInquiryInfo("inquirer_id", testInquiry.getInquiryId());
            assertEquals("getInquiryInfo() should retrieve the correct inquirer_id",
                    1, retrievedInquirerId.intValue());
        } catch (SQLException e) {
            fail("Error testing getInquiryInfo: " + e.getMessage());
        } finally {
            try {
                if (testInquiry != null) {
                    inquiryService.removeInquiry(testInquiry);
                }
            } catch (SQLException e) {
                fail("Error cleaning up after test: " + e.getMessage());
            }
        }
    }

    @Test
    public void testUpdateLastKnownLocation() {
        Inquiry testInquiry = null;
        int newLocationId = 2;

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), "Test inquiry");
            boolean updateResult = inquiryService.updateLastKnownLocation(testInquiry, newLocationId);

            assertTrue("updateLastKnownLocation() should return true for successful update", updateResult);
            assertEquals("updateLastKnownLocation() should update the location ID in the Inquiry object",
                    newLocationId, testInquiry.getLastKnownLocationId());

            Integer retrievedLocationId = (Integer) inquiryService.getInquiryInfo("location_id", testInquiry.getInquiryId());
            assertEquals("updateLastKnownLocation() should update the location ID in the database",
                    newLocationId, retrievedLocationId.intValue());
        } catch (SQLException e) {
            fail("Error testing updateLastKnownLocation: " + e.getMessage());
        } finally {
            try {
                if (testInquiry != null) {
                    inquiryService.removeInquiry(testInquiry);
                }
            } catch (SQLException e) {
                fail("Error cleaning up after test: " + e.getMessage());
            }
        }
    }

    @Test
    public void testRemoveInquiry() {
        Inquiry testInquiry = null;
        boolean removalResult = false;

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), "Test remove");
            int inquiryId = testInquiry.getInquiryId();
            removalResult = inquiryService.removeInquiry(testInquiry);

            assertTrue("removeInquiry() should return true when successful", removalResult);

            Inquiry retrievedInquiry = inquiryService.getInquiryById(inquiryId);
            assertNull("removeInquiry() should remove the inquiry from the database", retrievedInquiry);
        } catch (SQLException e) {
            fail("Error testing removeInquiry: " + e.getMessage());
        }
    }

    @Test
    public void testInquiryNullInfoProvided() {
        Inquiry testInquiry = null;
        String testInfo = null;

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), testInfo);
            assertNotNull("addInquiry() should handle null info", testInquiry);
            assertNull("Inquiry should have null infoProvided", testInquiry.getInfoProvided());
        } catch (SQLException e) {
            fail("Error testing inquiry with null info: " + e.getMessage());
        } finally {
            try {
                if (testInquiry != null) {
                    inquiryService.removeInquiry(testInquiry);
                }
            } catch (SQLException e) {
                fail("Error cleaning up after test: " + e.getMessage());
            }
        }
    }
}