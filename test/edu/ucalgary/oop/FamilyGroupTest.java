package edu.ucalgary.oop;

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

    @Test
    public void testFamilyGroupConstructorNoMembers() {
        FamilyGroup group1 = new FamilyGroup(10);
        assertEquals("Group ID should be set correctly", 10, group1.getGroupId());
    }

    @Test
    public void testFamilyGroupConstructorWithMembersId() {
        HashSet<Person> members = new HashSet<>();
        members.add(testPerson1);
        members.add(testPerson2);
        FamilyGroup group2 = new FamilyGroup(20, members);
        assertEquals("Group ID should be set correctly", 20, group2.getGroupId());
    }

    @Test
    public void testFamilyGroupConstructorWithMembersSet() {
        HashSet<Person> members = new HashSet<>();
        members.add(testPerson1);
        members.add(testPerson2);
        FamilyGroup group2 = new FamilyGroup(20, members);
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
    public void testSetCommonFamilyNameReturnValue() {
        boolean success = testFamilyGroup.setCommonFamilyName("Johnson");
        assertTrue("setCommonFamilyName should return true for valid name", success);
    }

    @Test
    public void testSetCommonFamilyNameValue() {
        testFamilyGroup.setCommonFamilyName("Johnson");
        assertEquals("Common family name should be updated to 'Johnson'", "Johnson", testFamilyGroup.getCommonFamilyName());
    }

    @Test
    public void testSetEmptyCommonFamilyName() {
        boolean success = testFamilyGroup.setCommonFamilyName("");
        assertFalse("An empty family name should not be allowed", success);
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
        assertEquals("setMembers() should properly set the members in the FamilyGroup", members, testFamilyGroup.getMembers());
    }

    @Test
    public void testSetEmptyMembers() {
        HashSet<Person> emptyMembers = new HashSet<>();
        testFamilyGroup.setMembers(emptyMembers);
        assertEquals("Members set should be empty", 0, testFamilyGroup.getMembers().size());
    }

    @Test
    public void testAddMemberPerson1() {
        testFamilyGroup.addMember(testPerson1);
        assertTrue("Members should contain testPerson1", testFamilyGroup.getMembers().contains(testPerson1));
    }

    @Test
    public void testAddMemberPerson2() {
        testFamilyGroup.addMember(testPerson2);
        assertTrue("Members should contain testPerson2", testFamilyGroup.getMembers().contains(testPerson2));
    }

    @Test
    public void testAddDuplicateMember() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.addMember(testPerson1);
        assertEquals("Adding a duplicate should not change the member count", 1, testFamilyGroup.getMembers().size());
    }

    @Test
    public void testRemoveMemberReturnValue() {
        testFamilyGroup.addMember(testPerson1);
        boolean removed = testFamilyGroup.removeMember(testPerson1);
        assertTrue("removeMember() should return true for successful removal", removed);
    }

    @Test
    public void testRemoveMemberVerifyRemoved() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.addMember(testPerson2);
        testFamilyGroup.removeMember(testPerson1);
        assertFalse("testPerson1 should be removed", testFamilyGroup.getMembers().contains(testPerson1));
    }

    @Test
    public void testRemoveMemberVerifySize() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.addMember(testPerson2);
        testFamilyGroup.removeMember(testPerson1);
        assertEquals("testFamilyGroup should have 1 member after removal", 1, testFamilyGroup.getMembers().size());
    }

    @Test
    public void testRemoveMemberNotInGroup() {
        boolean success = testFamilyGroup.removeMember(testPerson1);
        assertFalse("removeMember() should return false when trying to remove a member that isn't in the group", success);
    }

    @Test
    public void testRemoveLastMember() {
        testFamilyGroup.addMember(testPerson1);
        testFamilyGroup.removeMember(testPerson1);
        assertEquals("Members should be empty after removing last member", 0, testFamilyGroup.getMembers().size());
    }

    @Test
    public void testAddDisasterVictim() {
        DisasterVictim victim = new DisasterVictim(103, "Test DisasterVictim", "Non-binary person", LocalDate.now(), "333-3333");
        testFamilyGroup.addMember(victim);
        assertTrue("A DisasterVictim should be added as a member", testFamilyGroup.getMembers().contains(victim));
    }
}
