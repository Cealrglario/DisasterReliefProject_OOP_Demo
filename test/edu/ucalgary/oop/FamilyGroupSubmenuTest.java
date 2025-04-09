package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FamilyGroupSubmenuTest {
    private FamilyGroupSubmenu testSubmenu;
    private LanguageManager languageManager;

    @Before
    public void setUp() {
        languageManager = LanguageManager.INSTANCE;
        testSubmenu = new FamilyGroupSubmenu(languageManager.getMenuTranslation("family_group_submenu_defaults"));

        testSubmenu.setCurrentDisplay(testSubmenu.getDefaultOptions());
    }

    @Test
    public void getManageGroupOptions() {
        assertEquals("getManageGroupOptions() should retrieve MANAGE_GROUP_OPTIONS as expected",
                languageManager.getMenuTranslation("manage_family_group_options"), testSubmenu.getManageGroupOptions());
    }

    @Test
    public void listAllFamilyGroups() {
    }

    @Test
    public void viewFamilyMembers() {
    }

    @Test
    public void manageFamilyGroup() {
    }
}