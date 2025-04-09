package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DisasterVictimTest {
    private DisasterVictim testVictim1;
    private DisasterVictim testVictim2;
    private MedicalRecord testRecord;
    private PersonalBelonging testBelonging;

    @Before
    public void setUp() {
        testVictim1 = new DisasterVictim(101, "Victim 1", "Female",
                LocalDate.of(2025, 1, 1), "111-1111");
        testVictim2 = new DisasterVictim(102, "Victim 2", "Male", LocalDate.of(1995, 6, 10),
                LocalDate.of(2025, 1, 1), "222-2222");
        testRecord = new MedicalRecord(1, 1, "Test Record");
        testBelonging = new PersonalBelonging(1, "Test belonging");
    }

    @Test
    public void testDisasterVictimConstructor() {
        assertEquals("ASSIGNED_ID should be set correctly", 101, testVictim1.getAssignedId());
        assertEquals("First name should be set correctly", "Victim 1", testVictim1.getFirstName());
        assertEquals("Gender should be set correctly", "Female", testVictim1.getGender());
        assertEquals("Phone number should be set correctly", "111-1111", testVictim1.getPhoneNumber());
        assertEquals("Entry date should be set correctly", LocalDate.of(2025, 1, 1),
                testVictim1.getEntryDate());

        assertEquals("ASSIGNED_ID should be set correctly", 102, testVictim2.getAssignedId());
        assertEquals("First name should be set correctly", "Victim 2", testVictim2.getFirstName());
        assertEquals("Gender should be set correctly", "Male", testVictim2.getGender());
        assertEquals("Date of birth should be set correctly", LocalDate.of(1995, 6, 10),
                testVictim2.getDateOfBirth());
        assertEquals("Phone number should be set correctly", "222-2222", testVictim2.getPhoneNumber());
        assertEquals("Entry date should be set correctly", LocalDate.of(2025, 1, 1),
                testVictim2.getEntryDate());
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

        records.add(new MedicalRecord(2, 2, "Test record 2"));
        testVictim1.setMedicalRecords(records);

        List<MedicalRecord> newRecords = testVictim1.getMedicalRecords();

        assertNotEquals("Medical records should be updated correctly", newRecords, oldRecords);
    }

    @Test
    public void testGetPersonalBelongings() {
        List<PersonalBelonging> belongings = new ArrayList<>();

        belongings.add(testBelonging);
        testVictim1.setPersonalBelongings(belongings);

        assertEquals("getPersonalBelongings() should return the correct personal belongings",
                belongings, testVictim1.getPersonalBelongings());
    }

    @Test
    public void testSetPersonalBelongings() {
        List<PersonalBelonging> belongings = new ArrayList<>();

        belongings.add(testBelonging);
        testVictim1.setPersonalBelongings(belongings);

        List<PersonalBelonging> oldBelongings = testVictim1.getPersonalBelongings();

        belongings.add(new PersonalBelonging(2, "Test belonging 2"));
        testVictim1.setPersonalBelongings(belongings);

        List<PersonalBelonging> newBelongings = testVictim1.getPersonalBelongings();

        assertNotEquals("Medical records should be updated correctly", newBelongings, oldBelongings);
    }

    @Test
    public void testGetEntryDate() {
        assertEquals("getEntryDate() should return the correct entry date",
                LocalDate.of(2025, 1, 1), testVictim1.getEntryDate());
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
    public void testAddPersonalBelonging() {
        testVictim1.addPersonalBelonging(testBelonging);

        List<PersonalBelonging> belongings = testVictim1.getPersonalBelongings();

        assertTrue("Personal belonging should be added to the list",
                belongings.contains(testBelonging));
        assertEquals("List should contain 1 belonging",
                1, belongings.size());
    }

    @Test
    public void testRemovePersonalBelonging() {
        testVictim1.addPersonalBelonging(testBelonging);
        testVictim1.removePersonalBelonging(testBelonging);

        List<PersonalBelonging> belongings = testVictim1.getPersonalBelongings();

        assertFalse("Personal belonging should be removed from the list",
                belongings.contains(testBelonging));
        assertEquals("List should be empty",
                0, belongings.size());
    }

    @Test
    public void testAddMedicalRecord() {
        testVictim1.addMedicalRecord(testRecord);

        List<MedicalRecord> records = testVictim1.getMedicalRecords();

        assertTrue("Medical record should be added to the list",
                records.contains(testRecord));
        assertEquals("List should contain 1 record",
                1, records.size());
    }

    @Test
    public void testRemoveMedicalRecord() {
        testVictim1.addMedicalRecord(testRecord);
        testVictim1.removeMedicalRecord(testRecord);

        List<MedicalRecord> records = testVictim1.getMedicalRecords();

        assertFalse("Medical record should be removed from the list",
                records.contains(testRecord));
        assertEquals("List should be empty",
                0, records.size());
    }
}
