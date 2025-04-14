package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {
    private MainMenu testMainMenu;
    private LanguageManager languageManager;

    @Before
    public void setUp() {
        languageManager = LanguageManager.INSTANCE;

        // Initialize language manager with a valid language
        // This ensures translations are available
        languageManager.configureLanguage("en-CA");

        testMainMenu = new MainMenu(languageManager.getMenuTranslation("main_menu_defaults"));
        testMainMenu.setCurrentDisplay(testMainMenu.getDefaultOptions());
    }

    @Test
    public void testMenuConstructor() {
        assertEquals("Menu options should be set as expected", languageManager.getMenuTranslation("main_menu_defaults"),
                testMainMenu.getDefaultOptions());
    }

    @Test
    public void testGetCurrentDisplay() {
        assertEquals("getCurrentDisplay() should retrieve the currentDisplay of the menu", testMainMenu.getDefaultOptions(),
                testMainMenu.getCurrentDisplay());
    }

    @Test
    public void testSetCurrentDisplay() {
        String[] testNewDisplay = {"1. Test", "2. Test 2"};

        testMainMenu.setCurrentDisplay(testNewDisplay);

        assertEquals("setCurrentDisplay() should update the currentDisplay of the menu as expected",
                testNewDisplay, testMainMenu.getCurrentDisplay());
    }

    @Test
    public void testGetDefaultOptions() {
        assertEquals("getDefaultOptions() should retrieve the default menu options of the menu",
                languageManager.getMenuTranslation("main_menu_defaults"), testMainMenu.getDefaultOptions());
    }

    @Test
    public void testHandleDisplay() {
        String[] testDisplay = {"1. Test", "2. Test 2", "3. Test 3"};

        testMainMenu.setCurrentDisplay(testDisplay);
        testMainMenu.handleDisplay();

        // Not sure how to test this, as handleDisplay prints to the CLI
    }

    @Test
    public void testReturnToMainMenu() {
        // Not sure how to test this, since this relies entirely on MenuManager's implementation of returnToMainMenu()
    }

    @Test
    public void testHandleIntInput() {
        testMainMenu.setMinIntInput(1);
        testMainMenu.setMaxIntInput(5);
        // Not sure how to test this, as handleIntInput gets input from the user
    }

    @Test
    public void testHandleStringInput() {
        testMainMenu.setStringEmptyAllowed(false);
        testMainMenu.setStringNumbersAllowed(true);
        // Not sure how to test this, as handleStringInput gets input from the user
    }

    @Test
    public void testGetRequiresIntInput() {
        assertEquals("getRequiresIntInput() should return the current requiresIntInput value of the menu",
                true, testMainMenu.getRequiresIntInput());
    }

    @Test
    public void testSetRequiresIntInput() {
        testMainMenu.setRequiresIntInput(false);
        assertEquals("setRequiresIntInput() should update the requiresIntInput value of the menu as expected",
                false, testMainMenu.getRequiresIntInput());
    }

    @Test
    public void testGetMinIntInput() {
        testMainMenu.setMinIntInput(5);
        assertEquals("getMinIntInput() should return the current minIntInput value of the menu",
                5, testMainMenu.getMinIntInput());
    }

    @Test
    public void testSetMinIntInput() {
        testMainMenu.setMinIntInput(10);
        assertEquals("setMinIntInput() should update the minIntInput value of the menu as expected",
                10, testMainMenu.getMinIntInput());
    }

    @Test
    public void testGetMaxIntInput() {
        testMainMenu.setMaxIntInput(20);
        assertEquals("getMaxIntInput() should return the current maxIntInput value of the menu",
                20, testMainMenu.getMaxIntInput());
    }

    @Test
    public void testSetMaxIntInput() {
        testMainMenu.setMaxIntInput(30);
        assertEquals("setMaxIntInput() should update the maxIntInput value of the menu as expected",
                30, testMainMenu.getMaxIntInput());
    }

    @Test
    public void testGetStringEmptyAllowed() {
        testMainMenu.setStringEmptyAllowed(true);
        assertEquals("getStringEmptyAllowed() should return the current stringEmptyAllowed value of the menu",
                true, testMainMenu.getStringEmptyAllowed());
    }

    @Test
    public void testSetStringEmptyAllowed() {
        testMainMenu.setStringEmptyAllowed(false);
        assertEquals("setStringEmptyAllowed() should update the stringEmptyAllowed value of the menu as expected",
                false, testMainMenu.getStringEmptyAllowed());
    }

    @Test
    public void testGetStringNumbersAllowed() {
        testMainMenu.setStringNumbersAllowed(true);
        assertEquals("getStringNumbersAllowed() should return the current stringNumbersAllowed value of the menu",
                true, testMainMenu.getStringNumbersAllowed());
    }

    @Test
    public void testSetStringNumbersAllowed() {
        testMainMenu.setStringNumbersAllowed(false);
        assertEquals("setStringNumbersAllowed() should update the stringNumbersAllowed value of the menu as expected",
                false, testMainMenu.getStringNumbersAllowed());
    }
}
