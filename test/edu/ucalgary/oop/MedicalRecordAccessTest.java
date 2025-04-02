package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MedicalRecordAccessTest {
    private DatabaseConnectionManager connectionManager;
    private MedicalRecordAccess medicalRecordDbAccess;
    private Connection connection;

    MedicalRecord placeholderRecord = new MedicalRecord(-1, -1, "Placeholder");

    @Before
    public void setUp() throws Exception {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        medicalRecordDbAccess = new MedicalRecordAccess();

        medicalRecordDbAccess.addEntry(placeholderRecord);
    }

    @After
    public void tearDown() {
        medicalRecordDbAccess.removeEntry(placeholderRecord);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
        try {
            medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", medicalRecordDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<MedicalRecord> retrievedRecords;

        try {
            retrievedRecords = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of MedicalRecords", retrievedRecords.isEmpty());
    }

    @Test
    public void testGetById() {
        MedicalRecord testRecord;

        try {
            testRecord = medicalRecordDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("The MEDICAL_RECORD_ID of testRecord should match the id selected when calling getById()",
                -1, testRecord.getMedicalRecordId());
    }

    @Test
    public void testUpdateInfo() {
        MedicalRecord originalRecord;
        MedicalRecord updatedRecord;

        try {
            originalRecord = medicalRecordDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            medicalRecordDbAccess.updateInfo("treatment_details", "Updated treatment details");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            updatedRecord = medicalRecordDbAccess.getById(-1);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("MedicalRecord treatment details should be updated as expected",
                originalRecord.getTreatmentDetails(), updatedRecord.getTreatmentDetails());
    }

    @Test
    public void testGetInfo() {
        MedicalRecord testRecord;
        String retrievedDetails;

        try {
            testRecord = medicalRecordDbAccess.getById(-1);
            retrievedDetails = medicalRecordDbAccess.getInfo("treatment_details");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertEquals("Retrieved treatment details should match details in retrieved MedicalRecord",
                retrievedDetails, testRecord.getTreatmentDetails());
    }

    @Test
    public void testAddEntry() {
        List<MedicalRecord> recordsBeforeAdding;
        List<MedicalRecord> recordsAfterAdding;

        MedicalRecord newRecord = new MedicalRecord(-2, -1, "Test add record");

        try {
            recordsBeforeAdding = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            medicalRecordDbAccess.addEntry(newRecord);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            recordsAfterAdding = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("New medical record should be added in the database",
                recordsAfterAdding.size(), recordsBeforeAdding.size());

        try {
            medicalRecordDbAccess.removeEntry(newRecord);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<MedicalRecord> recordsBeforeRemoving;
        List<MedicalRecord> recordsAfterRemoving;

        MedicalRecord exRecord = new MedicalRecord(-2, -1, "test remove record");

        try {
            medicalRecordDbAccess.addEntry(exRecord);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            recordsBeforeRemoving = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        try {
            medicalRecordDbAccess.removeEntry(exRecord);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
    }

        try {
            recordsAfterRemoving = medicalRecordDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNotEquals("Removed medical record should no longer be in the database",
                recordsAfterRemoving.size(), recordsBeforeRemoving.size());
    }

    @Test
    public void testGetByNonExistentId() {
        MedicalRecord testRecord;

        try {
            testRecord = medicalRecordDbAccess.getById(-999);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertNull("A null object should be returned if attempting to retrieve a non-existent MedicalRecord",
                testRecord);
    }

    @Test
    public void testUpdateInfoWithInvalidField() {
        boolean success;

        try {
            success = medicalRecordDbAccess.updateInfo("non_existent_field", "test value");
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("updateInfo() should return false when trying to update a non-existent field",
                success);
    }

    @Test
    public void testRemoveEntryNotInDb() {
        boolean success;
        MedicalRecord recordNotInDb = new MedicalRecord(-2, -1, "Test not in db");

        try {
            success = medicalRecordDbAccess.removeEntry(recordNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing: " + e.getMessage());
        }

        assertFalse("removeEntry() should return false when trying to remove a MedicalRecord that isn't in the database",
                success);
    }
}
