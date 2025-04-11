package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class MedicalRecordAccessTest {
    private MedicalRecordAccess<String> medicalRecordDbAccess;

    MedicalRecord placeholderRecord;

    @Before
    public void setUp() throws Exception {
        medicalRecordDbAccess = new MedicalRecordAccess<>();

        try {
            placeholderRecord = medicalRecordDbAccess.addMedicalRecord(1, "Placeholder");
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            medicalRecordDbAccess.removeMedicalRecord(placeholderRecord);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
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
    public void testAddEntry() {
        List<MedicalRecord> recordsBeforeAdding = null;
        List<MedicalRecord> recordsAfterAdding = null;

        MedicalRecord newRecord = null;

        try {
            recordsBeforeAdding = medicalRecordDbAccess.getAll();
            newRecord = medicalRecordDbAccess.addMedicalRecord(2, "Test add record");
            recordsAfterAdding = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New MedicalRecord should be added in the database",
                recordsAfterAdding.size(), recordsBeforeAdding.size());

        try {
            medicalRecordDbAccess.removeMedicalRecord(newRecord);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<MedicalRecord> recordsBeforeRemoving = null;
        List<MedicalRecord> recordsAfterRemoving = null;

        MedicalRecord exRecord = null;

        try {
            exRecord = medicalRecordDbAccess.addMedicalRecord(2, "Test remove record");
            recordsBeforeRemoving = medicalRecordDbAccess.getAll();
            medicalRecordDbAccess.removeMedicalRecord(exRecord);
            recordsAfterRemoving = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Removed MedicalRecord should no longer be in the database",
                recordsAfterRemoving.size(), recordsBeforeRemoving.size());
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

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success = true;
        MedicalRecord recordNotInDb = new MedicalRecord(-999, -1, "Test not in db");

        try {
            success = medicalRecordDbAccess.removeMedicalRecord(recordNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a MedicalRecord that isn't in the database",
                success);
    }
}
