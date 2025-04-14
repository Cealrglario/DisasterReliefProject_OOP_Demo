package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DisasterVictimTest {
    private DisasterVictim testVictim1;
    private DisasterVictim testVictim2;
    private MedicalRecord testRecord;
    private Supply testSupply;

    @Before
    public void setUp() {
        // Create test victims using the constructors available in DisasterVictim
        testVictim1 = new DisasterVictim(101, "Victim 1", "Female", "111-1111");
        testVictim2 = new DisasterVictim(102, "Victim 2", "Male",
                LocalDate.of(1995, 6, 10), "222-2222");

        testRecord = new MedicalRecord(100, 100, "Test Record");
        testSupply = new Blanket(100);
    }

    @Test
    public void testDisasterVictimConstructor() {
        assertEquals("ASSIGNED_ID should be set correctly", 101, testVictim1.getAssignedId());
        assertEquals("First name should be set correctly", "Victim 1", testVictim1.getFirstName());
        assertEquals("Gender should be set correctly", "Female", testVictim1.getGender());
        assertEquals("Phone number should be set correctly", "111-1111", testVictim1.getPhoneNumber());

        assertEquals("ASSIGNED_ID should be set correctly", 102, testVictim2.getAssignedId());
        assertEquals("First name should be set correctly", "Victim 2", testVictim2.getFirstName());
        assertEquals("Gender should be set correctly", "Male", testVictim2.getGender());
        assertEquals("Date of birth should be set correctly", LocalDate.of(1995, 6, 10),
                testVictim2.getDateOfBirth());
        assertEquals("Phone number should be set correctly", "222-2222", testVictim2.getPhoneNumber());
    }

    @Test
    public void testGetMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        records.add(testRecord);
        testVictim1.setMedicalRecords(records);
        assertEquals("getMedicalRecords() should return the correct medical records",
                records, testVictim1.getMedicalRecords());
    }

    @Test
    public void testSetMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        records.add(testRecord);
        testVictim1.setMedicalRecords(records);
        List<MedicalRecord> oldRecords = testVictim1.getMedicalRecords();

        records.add(new MedicalRecord(200, 200, "Test record 2"));
        testVictim1.setMedicalRecords(records);
        List<MedicalRecord> newRecords = testVictim1.getMedicalRecords();

        assertTrue("Medical records should be updated correctly", newRecords.size() > oldRecords.size());
    }

    @Test
    public void testGetSupplies() {
        List<Supply> supplies = new ArrayList<>();
        supplies.add(testSupply);
        testVictim1.setSupplies(supplies);

        assertEquals("getSupplies() should return the correct supplies",
                supplies, testVictim1.getSupplies());
    }

    @Test
    public void testSetSupplies() {
        List<Supply> supplies = new ArrayList<>();
        supplies.add(testSupply);
        testVictim1.setSupplies(supplies);

        assertEquals("Supplies should be updated correctly", supplies, testVictim1.getSupplies());
    }

    @Test
    public void testGetComments() {
        String comments = "Test comment";
        testVictim1.setComments(comments);
        assertEquals("getComments() should return the correct comments",
                comments, testVictim1.getComments());
    }

    @Test
    public void testSetComments() {
        String comments = "Test comment";
        testVictim1.setComments(comments);
        String oldComments = testVictim1.getComments();

        String newComment = "New comment";
        testVictim1.setComments(newComment);
        String newComments = testVictim1.getComments();

        assertNotEquals("Comments should be updated correctly",
                oldComments, newComments);
    }

    @Test
    public void testAddSupply() {
        testVictim1.addSupply(testSupply);
        List<Supply> supplies = testVictim1.getSupplies();
        assertTrue("Supply should be added to the list",
                supplies.contains(testSupply));
    }

    @Test
    public void testRemoveSupply() {
        testVictim1.addSupply(testSupply);
        testVictim1.removeSupply(testSupply);
        List<Supply> supplies = testVictim1.getSupplies();
        assertFalse("Supply should be removed from the list",
                supplies.contains(testSupply));
    }

    @Test
    public void testAddMedicalRecord() {
        testVictim1.addMedicalRecord(testRecord);
        List<MedicalRecord> records = testVictim1.getMedicalRecords();
        assertTrue("Medical record should be added to the list",
                records.contains(testRecord));
    }

    @Test
    public void testRemoveMedicalRecord() {
        testVictim1.addMedicalRecord(testRecord);
        testVictim1.removeMedicalRecord(testRecord);
        List<MedicalRecord> records = testVictim1.getMedicalRecords();
        assertFalse("Medical record should be removed from the list",
                records.contains(testRecord));
    }
}
