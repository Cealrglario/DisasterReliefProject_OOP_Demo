package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;

public class InquiryTest {
    Inquiry testInquiry;

    @Before
    public void setUp() {
        testInquiry = new Inquiry(1, 1, 1,
                "Info test");
        testInquiry.setLastKnownLocationId(1);
    }

    @Test
    public void testInquiryConstructor() {
        assertEquals("INQUIRY_ID should be set as expected", 1, testInquiry.getInquiryId());
        assertEquals("INQUIRER_ID should be set as expected", 1, testInquiry.getInquirerid());
        assertEquals("MISSING_PERSON_ID should be set as expected", 1, testInquiry.getMissingPersonId());
        assertEquals("infoProvided should be set as expected", "Info test", testInquiry.getInfoProvided());
    }

    @Test
    public void testGetInquiryId() {
        assertEquals("INQUIRY_ID should be retrieved as expected", 1, testInquiry.getInquiryId());
    }

    @Test
    public void testGetInquirerid() {
        assertEquals("INQUIRER_ID should be retrieved as expected", 1, testInquiry.getInquirerid());
    }

    @Test
    public void testGetMissingPersonId() {
        assertEquals("MISSING_PERSON_ID should be retrieved as expected", 1, testInquiry.getMissingPersonId());
    }

    @Test
    public void testGetDateOfInquiry() {
        assertEquals("INQUIRY_DATE should be retrieved as expected", LocalDate.now(), testInquiry.getDateOfInquiry());
    }

    @Test
    public void testGetInfoProvided() {
        assertEquals("infoProvided should be retrieved as expected", "Info test", testInquiry.getInfoProvided());
    }

    @Test
    public void testSetInfoProvided() {
        testInquiry.setInfoProvided("Updated info");
        assertEquals("infoProvided should be updated as expected", "Updated info", testInquiry.getInfoProvided());
    }

    @Test
    public void testGetLastKnownLocationId() {
        assertEquals("lastKnownLocationId should be retrieved as expected", 1, testInquiry.getLastKnownLocationId());
    }

    @Test
    public void testSetLastKnownLocationId() {
        testInquiry.setLastKnownLocationId(10);
        assertEquals("lastKnownLocationId should be updated as expected", 10, testInquiry.getLastKnownLocationId());
    }
}
