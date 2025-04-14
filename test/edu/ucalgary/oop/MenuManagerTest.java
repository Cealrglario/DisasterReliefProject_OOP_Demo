package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuManagerTest {
    private MenuManager menuManager;
    private LanguageManager languageManager;
    private MainMenu testMainMenu;
    private InquirySubmenu testInquirySubmenu;

    @Before
    public void setUp() {
        menuManager = MenuManager.INSTANCE;
        languageManager = LanguageManager.INSTANCE;
        testMainMenu = new MainMenu(languageManager.getMenuTranslation("main_menu_defaults"));
        testInquirySubmenu = new InquirySubmenu(languageManager.getMenuTranslation("inquiry_submenu_defaults"));

        menuManager.navigateToMenu(testMainMenu);
    }

    @After
    public void tearDown() {
        menuManager.returnToMainMenu();
    }

    @Test
    public void testGetCurrentMenu() {
        assertEquals("getCurrentMenu() should retrieve the current displayed menu", testMainMenu,
                menuManager.getCurrentMenu());
    }

    @Test
    public void testNavigateToMenu() {
        menuManager.navigateToMenu(testInquirySubmenu);

        assertEquals("MenuManager should be navigated to the submenu for Family Group management",
                testInquirySubmenu, menuManager.getCurrentMenu());
    }

    @Test
    public void testBacktrackMenus() {
        menuManager.navigateToMenu(testInquirySubmenu);
        menuManager.backtrackMenus();

        assertEquals("backtrackMenus() should return to the previously displayed menu", testMainMenu,
                menuManager.getCurrentMenu());
    }

    @Test
    public void testGetIsRunning() {
        assertFalse("getIsRunning() should return a valid boolean", menuManager.getIsRunning());
    }

    @Test
    public void testStartRunning() {
        menuManager.startRunning();

        assertTrue("isRunning should be true after calling startRunning()", menuManager.getIsRunning());
    }

    @Test
    public void testStopRunning() {
        menuManager.stopRunning();

        assertFalse("isRunning should be false after calling stopRunning()", menuManager.getIsRunning());
    }
}