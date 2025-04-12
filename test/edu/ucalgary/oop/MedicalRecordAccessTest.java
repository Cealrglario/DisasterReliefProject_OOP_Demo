package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class MedicalRecordAccessTest {
    private MedicalRecordAccess<String> medicalRecordDbAccess;

    @Before
    public void setUp() throws Exception {
        medicalRecordDbAccess = new MedicalRecordAccess<>();
    }

    @Test
    public void testGetQueryResults() {
        try {
            medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", medicalRecordDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<MedicalRecord> retrievedRecords = null;

        try {
            retrievedRecords = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of MedicalRecords", retrievedRecords.isEmpty());
    }

    @Test
    public void testGetById() {
        MedicalRecord testRecord = null;

        try {
            testRecord = medicalRecordDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }

        assertEquals("The MEDICAL_RECORD_ID of testRecord should match the id selected when calling getById()",
                1, testRecord.getMedicalRecordId());
    }

    @Test
    public void testUpdateInfo() {
        MedicalRecord updatedRecord = null;

        try {
            medicalRecordDbAccess.updateInfo("treatment_details", "Updated treatment details", 1);
            updatedRecord = medicalRecordDbAccess.getById(1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfo: " + e.getMessage());
        }

        assertEquals("MedicalRecord treatment details should be updated as expected",
                "Updated treatment details", updatedRecord.getTreatmentDetails());
    }

    @Test
    public void testGetInfo() {
        MedicalRecord testRecord = null;
        String retrievedDetails = null;

        try {
            testRecord = medicalRecordDbAccess.getById(1);
            retrievedDetails = medicalRecordDbAccess.getInfo("treatment_details", 1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getInfo: " + e.getMessage());
        }

        assertEquals("Retrieved treatment details should match details in retrieved MedicalRecord",
                retrievedDetails, testRecord.getTreatmentDetails());
    }


    @Test
    public void testGetByIdNotInDb() {
        MedicalRecord testRecord = null;

        try {
            testRecord = medicalRecordDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve a MedicalRecord that isn't in the database",
                testRecord);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success = true;

        try {
            success = medicalRecordDbAccess.updateInfo("non_existent_field", "test value", 1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing updateInfoWithInvalidField: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }


}
