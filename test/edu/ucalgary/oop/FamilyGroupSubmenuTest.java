package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class FamilyGroupSubmenuTest {
    private FamilyGroupSubmenu testSubmenu;
    private LanguageManager languageManager;

    private final ByteArrayOutputStream captureOutput = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        languageManager = LanguageManager.INSTANCE;
        testSubmenu = new FamilyGroupSubmenu(languageManager.getMenuTranslation("family_group_submenu_defaults"));
        testSubmenu.setCurrentDisplay(testSubmenu.getDefaultOptions());

        System.setOut(new PrintStream(captureOutput));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void getManageGroupOptions() {
        assertEquals("getManageGroupOptions() should retrieve MANAGE_GROUP_OPTIONS as expected",
                languageManager.getMenuTranslation("manage_family_group_options"), testSubmenu.getManageGroupOptions());
    }

    @Test
    public void listAllFamilyGroups() {
        captureOutput.reset();

        Person testPerson = new Person(1, "Test", "Male", "111-1111");
        FamilyGroup testFamilyGroup = new FamilyGroup(1);
        testFamilyGroup.setCommonFamilyName("Test Family");
        testFamilyGroup.addMember(testPerson);

        PersonAccess<Integer> personDbAccess = new PersonAccess<>();
        personDbAccess.addEntry(testPerson);
        personDbAccess.getAllFamilyGroups();

        testSubmenu.listAllFamilyGroups();

        String printedOutput = captureOutput.toString();

        assertTrue("listAllFamilyGroups() prints out a list of family groups and their id",
                printedOutput.contains("Test Family, ID: 1"));
    }

    @Test
    public void viewFamilyMembers() {
        captureOutput.reset();

        Person testPerson = new Person(1, "Test", "Male", "111-1111");
        FamilyGroup testFamilyGroup = new FamilyGroup(1);
        testFamilyGroup.setCommonFamilyName("Test Family");
        testFamilyGroup.addMember(testPerson);

        PersonAccess<Integer> personDbAccess = new PersonAccess<>();
        personDbAccess.addEntry(testPerson);
        personDbAccess.getAllFamilyGroups();

        testSubmenu.viewFamilyMembers();

        String printedOutput = captureOutput.toString();

        assertTrue("viewFamilyMembers() prints out a list of family members and their id",
                printedOutput.contains("Test, ID: 1"));
    }

    @Test
    public void manageFamilyGroup() {
        // TO DO
    }
}