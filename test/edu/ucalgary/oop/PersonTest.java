package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class PersonTest {
    private Person testPerson1;
    private Person testPerson2;
    private FamilyGroup testFamilyGroup;

    @Before
    public void setUp() {
        testPerson1 = new Person(101, "Test 1", "Female", "111-1111");
        testPerson2 = new Person(102, "Test 2", "Male", LocalDate.of(1990, 1, 1),
                "222-2222");
        testFamilyGroup = new FamilyGroup(1);

        testPerson1.setLastName("Test last name");
        testPerson2.setLastName("Test last name");
    }

    @Test
    public void testPersonConstructor() {
        assertEquals("ASSIGNED_ID should be set correctly", 101, testPerson1.getAssignedId());
        assertEquals("First name should be set correctly", "Test 1", testPerson1.getFirstName());
        assertEquals("Gender should be set correctly", "Female", testPerson1.getGender());
        assertEquals("Phone number should be set correctly", "111-1111", testPerson1.getPhoneNumber());

        assertEquals("ASSIGNED_ID should be set correctly", 102, testPerson2.getAssignedId());
        assertEquals("First name should be set correctly", "Test 2", testPerson2.getFirstName());
        assertEquals("Gender should be set correctly", "Male", testPerson2.getGender());
        assertEquals("Date of birth should be set correctly", LocalDate.of(1990, 1, 1),
                testPerson2.getDateOfBirth());
        assertEquals("Phone number should be set correctly", "222-2222", testPerson2.getPhoneNumber());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("getFirstName() should return the correct first name", "Test 1", testPerson1.getFirstName());
    }

    @Test
    public void testSetFirstName() {
        testPerson1.setFirstName("New name");
        assertEquals("First name should be updated to 'New name'", "New name", testPerson1.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("getLastName() should return the correct last name", "Test last name", testPerson1.getLastName());
    }

    @Test
    public void testSetLastName() {
        testPerson1.setLastName("New last name");
        assertEquals("Last name should be updated to 'New last name'", "New last name", testPerson1.getLastName());
    }

    @Test
    public void testGetDateOfBirth() {
        assertEquals("getDateOfBirth() should return the correct date of birth", LocalDate.of(1990, 1, 1),
                testPerson2.getDateOfBirth());
    }

    @Test
    public void testSetDateOfBirth() {
        LocalDate newDob = LocalDate.of(1985, 3, 15);
        testPerson1.setDateOfBirth(LocalDate.of(1985, 3, 15));
        assertEquals("Date of birth should be updated correctly", newDob, testPerson1.getDateOfBirth());
    }

    @Test
    public void testGetGender() {
        assertEquals("getGender() should return the correct gender", "Female", testPerson1.getGender());
    }

    @Test
    public void testSetGender() {
        testPerson1.setGender("Non-binary");
        assertEquals("Gender should be updated to 'Non-binary'", "Non-binary", testPerson1.getGender());
    }

    @Test
    public void testGetInFamilyGroup() {
        assertFalse("getInFamilyGroup() should return false by default", testPerson1.getInFamilyGroup());
    }

    @Test
    public void testSetInFamilyGroup() {
        testPerson1.setInFamilyGroup(true);
        assertTrue("inFamilyGroup should be updated to true", testPerson1.getInFamilyGroup());
    }

    @Test
    public void testGetPhoneNumber() {
        assertEquals("getPhoneNumber() should return the correct phone number", "111-1111", testPerson1.getPhoneNumber());
    }

    @Test
    public void testSetPhoneNumber() {
        testPerson1.setPhoneNumber("555-5555");
        assertEquals("Phone number should be updated to '555-5555'", "555-5555", testPerson1.getPhoneNumber());
    }

    @Test
    public void testGetAssignedId() {
        assertEquals("getAssignedId() should return the correct ID", 101, testPerson1.getAssignedId());
    }
}
