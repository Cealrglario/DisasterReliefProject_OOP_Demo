package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.Assert.*;

public class FamilyGroupTest {
    private FamilyGroup testFamilyGroup;
    private Person testPerson1;
    private Person testPerson2;

    @Before
    public void setUp() {
        testFamilyGroup = new FamilyGroup(1);
        testPerson1 = new Person(101, "Test 1", "Female", "111-1111");
        testPerson2 = new Person(102, "Test 2", "Male", "222-2222");
    }

    @After
    public void tearDown() {
        testFamilyGroup = null;
        testPerson1 = null;
        testPerson2 = null;
    }

    @Test
    public void testFamilyGroupConstructorNoMembers() {
        FamilyGroup group1 = new FamilyGroup(10);

        assertEquals("Group ID should be set correctly", 10, group1.getGroupId());
    }

    @Test
    public void testFamilyGroupConstructorwithMembers() {
        HashSet<Person> members = new HashSet<>();
        members.add(testPerson1);
        members.add(testPerson2);
        FamilyGroup group2 = new FamilyGroup(20, members);

        assertEquals("Group ID should be set correctly", 20, group2.getGroupId());
        assertEquals("Members set should be assigned correctly", members, group2.getMembers());
    }

    @Test
    public void testGetGroupId() {
        assertEquals("getGroupId() should return the correct group ID", 1, testFamilyGroup.getGroupId());
    }

    @Test
    public void testGetCommonFamilyName() {
        testFamilyGroup.setCommonFamilyName("Smith");
        assertEquals("Common family name should be updated to 'Smith'", "Smith", testFamilyGroup.getCommonFamilyName());
    }

    @Test
    public void testSetCommonFamilyName() {
        testFamilyGroup.setCommonFamilyName("Johnson");
        assertEquals("Common family name should be updated to 'Johnson'", "Johnson", testFamilyGroup.getCommonFamilyName());
    }

    @Test
    public void testSetEmptyCommonFamilyName() {
        boolean success;

        success = testFamilyGroup.setCommonFamilyName("");
        assertFalse("An empty family name should not be allowed", success);
    }

    @Test
    public void testSetNullCommonFamilyName() {
        boolean success;

        success = testFamilyGroup.setCommonFamilyName(null);
        assertFalse("A null family name should not be allowed", success);
    }

    @Test
    public void testGetMembers() {
        HashSet<Person> members = new HashSet<>();
        members.add(testPerson1);
        testFamilyGroup.setMembers(members);

        assertEquals("getMembers() should return the assigned set of members", members, testFamilyGroup.getMembers());
    }

    @Test
    public void testSetMembers() {
        HashSet<Person> members = new HashSet<>();
        members.add(testPerson1);
        members.add(testPerson2);

        testFamilyGroup.setMembers(members);

        assertNotEquals("setMembers() should properly set the members in the FamilyGroup", 0,
                testFamilyGroup.getMembers().size());
    }

    @Test
    public void testSetEmptyMembers() {
        HashSet<Person> emptyMembers = new HashSet<>();
        testFamilyGroup.setMembers(emptyMembers);

        assertEquals("Members set should be empty", 0, testFamilyGroup.getMembers().size());
    }

    @Test
    public void testSetNullMembers() {
        boolean success;

        success = testFamilyGroup.setMembers(null);

        assertFalse("Null members should not be allowed", success);
    }

    @Test
    public void testAddMember() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.addMember(testPerson2);

        assertTrue("Members should contain testPerson1", testFamilyGroup.getMembers().contains(testPerson1));
        assertTrue("Members should contain testPerson2", testFamilyGroup.getMembers().contains(testPerson2));
    }

    @Test
    public void addDuplicateMember() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.addMember(testPerson1);

        assertEquals("Adding a duplicate should not change the member count", 1, testFamilyGroup.getMembers().size());
    }

    @Test
    public void addNullMember() {
        boolean success;

        success = testFamilyGroup.addMember(null);

        assertFalse("Null members should not be addable", success);
    }

    @Test
    public void testRemoveMember() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.addMember(testPerson2);

        testFamilyGroup.removeMember(testPerson1);

        assertFalse("testPerson1 should be removed", testFamilyGroup.getMembers().contains(testPerson1));
        assertEquals("testFamilyGroup should have 1 member after removal", 1, testFamilyGroup.getMembers().size());
    }

    @Test
    public void testRemoveMemberNotInDb() {
        boolean success;

        success = testFamilyGroup.removeMember(testPerson1);

        assertFalse("removeMember() should return false when trying to remove a member that isn't in the database",
                success);
    }

    @Test
    public void removeLastMember() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.removeMember(testPerson1);

        assertEquals("Members should be empty after removing last member", 0,
                testFamilyGroup.getMembers().size());
    }

    @Test
    public void testAddDisasterVictim() {
        DisasterVictim victim = new DisasterVictim(103, "Test DisasterVictim", "Male",
                LocalDate.now(), "333-3333");
        testFamilyGroup.addMember(victim);

        assertTrue("A DisasterVictim should be added as a member", testFamilyGroup.getMembers().contains(victim));
    }
}
