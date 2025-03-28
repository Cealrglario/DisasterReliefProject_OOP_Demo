package edu.ucalgary.oop;

import java.util.Map;
import java.util.regex.Pattern;

public enum LanguageManager {
    INSTANCE;

    private String currentLanguage;
    private final Pattern LANGUAGE_FORMAT = Pattern.compile("^[a-z]{2}-[A-Z]{2}$");
    private final String DEFAULT_LANGUAGE = "en-CA";
    private final String TRANSLATIONS_DIRECTORY = "data\\";
    private Map<String, String> translationData;

    public void configureLanguage() {}

    public boolean validateLanguage(String language) {
        return false;
    }

    public String getTranslation(String key) {
        return null;
    }

    public String getCurrentLanguage() {
        return this.currentLanguage;
    }

    public void setCurrentLanguage(String newLanguage) {}

    private boolean parseLanguageFile() {
        return false;
    }
}
