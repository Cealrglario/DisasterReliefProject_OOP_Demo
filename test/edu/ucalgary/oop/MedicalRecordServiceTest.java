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
    public void testUpdateTreatmentDetails() {
        MedicalRecord testMedicalRecord = null;
        String newTreatmentDetails = "Updated Treatment";
        String retrievedTreatmentDetails = null;

        try {
            testMedicalRecord = medicalRecordService.getMedicalRecordById(1);
            medicalRecordService.updateTreatmentDetails(testMedicalRecord, newTreatmentDetails);

            retrievedTreatmentDetails = (String) medicalRecordAccess.getInfo("treatment_details",
                    testMedicalRecord.getMedicalRecordId());
        } catch (SQLException e) {
            fail("Error testing updateTreatmentDetails: " + e.getMessage());
        }

        assertEquals("updateTreatmentDetails() should update the treatment details in-memory and in database",
                testMedicalRecord.getTreatmentDetails(), retrievedTreatmentDetails);
    }
}
