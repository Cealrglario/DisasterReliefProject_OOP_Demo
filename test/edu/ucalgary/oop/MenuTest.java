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
    public void testHandleIntInput() {
        testMainMenu.handleIntInput();

        // Not entirely sure how I could test this
    }

    @Test
    public void testHandleStringInput() {
        testMainMenu.handleStringInput();

        // Not entirely sure how I could test this
    }

    @Test
    public void testReturnToMainMenu() {
        // Not sure how to test this, since this relies entirely on MenuManager's implementation of returnToMainMenu()
    }
}
