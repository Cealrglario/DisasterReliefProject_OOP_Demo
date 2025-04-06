package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class MedicalRecordTest {
    MedicalRecord testMedicalRecord;

    @Before
    public void setUp() {
        testMedicalRecord = new MedicalRecord(1, 1, "Treatment test");
    }

    @Test
    public void testMedicalRecordConstructor() {
        assertEquals("MEDICAL_RECORD_ID should be set as expected", 1, testMedicalRecord.getMedicalRecordId());
        assertEquals("LOCATION_ID should be set as expected", 1, testMedicalRecord.getLocationId());
        assertEquals("treatmentDetails should be set as expected", "Treatment test", testMedicalRecord.getTreatmentDetails());
        assertEquals("TREATMENT_DATE should be set as expected", LocalDate.now(), testMedicalRecord.getTreatmentDate());
    }

    @Test
    public void testGetLocationId() {
        assertEquals("LOCATION_ID should be retrieved as expected", 1, testMedicalRecord.getLocationId());
    }

    @Test
    public void testGetMedicalRecordId() {
        assertEquals("MEDICAL_RECORD_ID should be retrieved as expected", 1, testMedicalRecord.getMedicalRecordId());
    }

    @Test
    public void testGetTreatmentDetails() {
        assertEquals("treatmentDetails should be retrieved as expected", "Treatment test", testMedicalRecord.getTreatmentDetails());
    }

    @Test
    public void testSetTreatmentDetails() {
        testMedicalRecord.setTreatmentDetails("Updated treatment");
        assertEquals("treatmentDetails should be updated as expected", "Updated treatment", testMedicalRecord.getTreatmentDetails());
    }

    @Test
    public void testGetTreatmentDate() {
        assertEquals("TREATMENT_DATE should be retrieved as expected", LocalDate.now(), testMedicalRecord.getTreatmentDate());
    }
}
