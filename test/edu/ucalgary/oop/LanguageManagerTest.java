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
        String expectedTranslation;
        languageManager.configureLanguage("en-CA");

        expectedTranslation = languageManager.getTranslation("Test");

        assertEquals("getTranslation() should retrieve the expected translation", "Test",
                expectedTranslation);
    }
}
