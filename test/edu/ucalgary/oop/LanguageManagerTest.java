package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LanguageManagerTest {
    LanguageManager languageManager;

    @Before
    public void setUp() {
        languageManager = LanguageManager.INSTANCE;
    }

    @Test
    public void testGetCurrentLanguage() {
        languageManager.configureLanguage("ru-RU");

        assertEquals("getCurrentLanguage() should retrieve the currentLanguage", "ru-RU",
                languageManager.getCurrentLanguage());
    }

    @Test
    public void testConfigureLanguage() {
        assertTrue(languageManager.configureLanguage("ru-RU"));
        assertEquals("LanguageManager should be configured to the selected language",
                "ru-RU", languageManager.getCurrentLanguage());
    }

    @Test
    public void testConfigureInvalidLanguage() {
        assertTrue(languageManager.configureLanguage("English"));
        assertEquals("LanguageManager should default to en-CA when receiving an invalid language as input",
                "en-CA", languageManager.getCurrentLanguage());
    }

    @Test
    public void testGetTranslation() {
        String retrievedTranslation;
        languageManager.configureLanguage("en-CA");

        retrievedTranslation = languageManager.getTranslation("test");

        assertEquals("getTranslation() should retrieve the expected translation", "test",
                retrievedTranslation);
    }

    @Test
    public void testGetMenuTranslation() {
        String[] expectedTranslation = {"1. Test Option 1", "2. Test Option 2"};
        String[] actualTranslation;

        languageManager.configureLanguage("en-CA");

        actualTranslation = languageManager.getMenuTranslation("test");

        assertEquals("getMenuTranslation() should retrieve the expected menu translation", expectedTranslation,
                actualTranslation);
    }
}
