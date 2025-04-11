package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InquiryServiceTest {
    private InquiryService inquiryService;
    private InquiryAccess<Object> inquiryAccess;

    @Before
    public void setUp() {
        inquiryService = InquiryService.INSTANCE;
        inquiryAccess = new InquiryAccess<>();
    }

    @Test
    public void testGetInquiryById() {
        Inquiry testInquiry = null;

        try {
            testInquiry = inquiryService.getInquiryById(1);
        } catch (SQLException e) {
            fail("Error testing getInquiryById: " + e.getMessage());
        }

        assertNotNull("getInquiryById() should retrieve a valid Inquiry", testInquiry);
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

        assertNotNull("getAllInquiries() should retrieve a valid list of Inquiries", retrievedInquiries);
        assertFalse("getAllInquiries() should retrieve at least one Inquiry", retrievedInquiries.isEmpty());
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
            fail("Error testing addInquiry: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateInquiryInfo() {
        Inquiry testInquiry = null;
        String newInfo = "Updated inquiry information";
        String retrievedInfo = null;

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), "Test inquiry");
            inquiryService.updateInquiryInfo(testInquiry, newInfo);

            retrievedInfo = (String) inquiryAccess.getInfo("comments", testInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("Error testing updateInquiryInfo: " + e.getMessage());
        }

        assertEquals("updateInquiryInfo() should update the info in-memory and in database",
                testInquiry.getInfoProvided(), retrievedInfo);

        try {
            inquiryService.removeInquiry(testInquiry);
        } catch (SQLException e) {
            fail("Error testing updateInquiryInfo: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateLastKnownLocation() {
        Inquiry testInquiry = null;
        int newLocationId = 2;
        Integer retrievedLocationId = null;

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), "Test inquiry");
            inquiryService.updateLastKnownLocation(testInquiry, newLocationId);

            retrievedLocationId = (Integer) inquiryAccess.getInfo("location_id", testInquiry.getInquiryId());
        } catch (SQLException e) {
            fail("Error testing updateLastKnownLocation: " + e.getMessage());
        }

        assertEquals("updateLastKnownLocation() should update the location ID in-memory and in database",
                testInquiry.getLastKnownLocationId(), retrievedLocationId.intValue());

        try {
            inquiryService.removeInquiry(testInquiry);
        } catch (SQLException e) {
            fail("Error testing updateLastKnownLocation: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveInquiry() {
        Inquiry testInquiry = null;
        boolean removalResult = false;

        try {
            testInquiry = inquiryService.addInquiry(1, 2, LocalDate.now(), "Test remove");
            removalResult = inquiryService.removeInquiry(testInquiry);
        } catch (SQLException e) {
            fail("Error testing removeInquiry: " + e.getMessage());
        }

        assertTrue("removeInquiry() should return true when successful", removalResult);
    }
}
