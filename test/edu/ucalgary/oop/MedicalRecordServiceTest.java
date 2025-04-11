package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class MedicalRecordServiceTest {
    private MedicalRecordService medicalRecordService;
    private MedicalRecordAccess<Object> medicalRecordAccess;

    @Before
    public void setUp() {
        medicalRecordService = MedicalRecordService.INSTANCE;
        medicalRecordAccess = new MedicalRecordAccess<>();
    }

    @Test
    public void testGetMedicalRecordById() {
        MedicalRecord testMedicalRecord = null;

        try {
            testMedicalRecord = medicalRecordService.getMedicalRecordById(1);
        } catch (SQLException e) {
            fail("Error testing getMedicalRecordById: " + e.getMessage());
        }

        assertEquals("getMedicalRecordById() should retrieve the correct MedicalRecord", 1,
                testMedicalRecord.getMedicalRecordId());
    }

    @Test
    public void testGetAllMedicalRecords() {
        List<MedicalRecord> retrievedMedicalRecords = null;

        try {
            retrievedMedicalRecords = medicalRecordService.getAllMedicalRecords();
        } catch (SQLException e) {
            fail("Error testing getAllMedicalRecords: " + e.getMessage());
        }

        assertNotNull("getAllMedicalRecords() should retrieve a valid list of MedicalRecords", retrievedMedicalRecords);
    }

    @Test
    public void testAddMedicalRecord() {
        MedicalRecord testMedicalRecord = null;
        int testLocationId = 1;
        String testTreatmentDetails = "Test Treatment";

        try {
            testMedicalRecord = medicalRecordService.addMedicalRecord(testLocationId, testTreatmentDetails);
        } catch (SQLException e) {
            fail("Error testing addMedicalRecord: " + e.getMessage());
        }

        assertNotNull("addMedicalRecord() should create and return a valid MedicalRecord", testMedicalRecord);
        assertEquals("addMedicalRecord() should set the locationId correctly", testLocationId, testMedicalRecord.getLocationId());
        assertEquals("addMedicalRecord() should set the treatmentDetails correctly", testTreatmentDetails,
                testMedicalRecord.getTreatmentDetails());

        try {
            medicalRecordService.removeMedicalRecord(testMedicalRecord);
        } catch (SQLException e) {
            fail("Error testing addMedicalRecord: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateTreatmentDetails() {
        MedicalRecord testMedicalRecord = null;
        String newTreatmentDetails = "Updated Treatment";
        String retrievedTreatmentDetails = null;

        try {
            testMedicalRecord = medicalRecordService.addMedicalRecord(1, "Initial Treatment");
            medicalRecordService.updateTreatmentDetails(testMedicalRecord, newTreatmentDetails);

            retrievedTreatmentDetails = (String) medicalRecordAccess.getInfo("treatment_details",
                    testMedicalRecord.getMedicalRecordId());
        } catch (SQLException e) {
            fail("Error testing updateTreatmentDetails: " + e.getMessage());
        }

        assertEquals("updateTreatmentDetails() should update the treatment details in-memory and in database",
                testMedicalRecord.getTreatmentDetails(), retrievedTreatmentDetails);

        try {
            medicalRecordService.removeMedicalRecord(testMedicalRecord);
        } catch (SQLException e) {
            fail("Error testing updateTreatmentDetails: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveMedicalRecord() {
        MedicalRecord testMedicalRecord = null;
        boolean removalResult = false;

        try {
            testMedicalRecord = medicalRecordService.addMedicalRecord(1, "Test Remove");
            removalResult = medicalRecordService.removeMedicalRecord(testMedicalRecord);
        } catch (SQLException e) {
            fail("Error testing removeMedicalRecord: " + e.getMessage());
        }

        assertTrue("removeMedicalRecord() should return true when successful", removalResult);
    }
}
