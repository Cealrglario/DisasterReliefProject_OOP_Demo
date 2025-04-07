package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {
    private MainMenu testMenu;

    @Before
    public void setUp() {
        String[] testDefaultOptions = {"1. Manage Persons", "2. Manage Locations", "3. Manage Inquiries",
        "4. Manage Family Groups", "5. Manage Supplies"};

        testMenu = new MainMenu(testDefaultOptions);
    }

    @Test
    public void testMainMenuConstructor() {
        String[] testDefaultOptions = {"1. Manage Persons", "2. Manage Locations", "3. Manage Inquiries",
                "4. Manage Family Groups", "5. Manage Supplies"};

        assertEquals("MainMenu options should be set as expected", testDefaultOptions, testMenu.getDefaultOptions());
    }
}
