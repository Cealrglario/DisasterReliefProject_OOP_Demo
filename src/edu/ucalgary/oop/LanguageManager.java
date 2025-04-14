package edu.ucalgary.oop;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LanguageManager {
    INSTANCE;

    private String currentLanguage;
    private final Pattern LANGUAGE_FORMAT = Pattern.compile("^[a-z]{2}-[A-Z]{2}$");
    private final String DEFAULT_LANGUAGE = "en-CA";
    private final String TRANSLATIONS_DIRECTORY = "data/";
    private Map<String, String> translationData = new HashMap<>();
    private Map<String, String[]> menuTranslationData = new HashMap<>();

    public String getCurrentLanguage() {
        return this.currentLanguage;
    }

    public boolean configureLanguage(String language) {
        currentLanguage = language;
        Matcher validateLanguage = LANGUAGE_FORMAT.matcher(language);

        if(!validateLanguage.matches()) {
            System.out.println("Could not parse desired language. Using default language of: " + DEFAULT_LANGUAGE);
            currentLanguage = DEFAULT_LANGUAGE;
        }

        if(!parseLanguageFile() && !currentLanguage.equals(DEFAULT_LANGUAGE)) {
            System.out.println("Could not parse language file. Reverting to default langauge of: " + DEFAULT_LANGUAGE);
            currentLanguage = DEFAULT_LANGUAGE;
        }

        if(!parseLanguageFile()) {
            System.out.println("Unable to configure language. Exiting program.");
            return false;
        }

        return true;
     }

    private boolean parseLanguageFile() {
        try {
            File translationFile = new File(TRANSLATIONS_DIRECTORY + currentLanguage + ".xml");

            if (!translationFile.exists()) {
                System.out.println("Translation file not found at: " + TRANSLATIONS_DIRECTORY + currentLanguage + ".xml");
                return false;
            }

            // Clean up just in case a language gets configured more than once
            translationData.clear();
            menuTranslationData.clear();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document parsedTranslation = builder.parse(translationFile);

            // .normalize() removes empty spaces in translationFile for easier parsing
            parsedTranslation.getDocumentElement().normalize();

            NodeList retrievedTranslations = parsedTranslation.getElementsByTagName("translation");

            for (int i = 0; i < retrievedTranslations.getLength(); i++) {
                Node node = retrievedTranslations.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // The nodes have the 'Element' type ONCE RETRIEVED, but the nodes themselves haven't been cast to the Element type yet.
                    // Casting is done here so that we can perform methods like .getAttribute() or getTextContent()
                    Element element = (Element) node;

                    String key = element.getElementsByTagName("key").item(0).getTextContent();
                    String translation = element.getElementsByTagName("value").item(0).getTextContent();

                    translationData.put(key, translation);
                }
            }

            NodeList retrievedMenuTranslations = parsedTranslation.getElementsByTagName("menuTranslation");

            for (int i = 0; i < retrievedMenuTranslations.getLength(); i++) {
                Node node = retrievedMenuTranslations.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    // The nodes have the 'Element' type ONCE RETRIEVED, but the nodes themselves haven't been cast to the Element type yet.
                    // Casting is done here so that we can perform methods like .getAttribute() or getTextContent()
                    Element element = (Element) node;

                    String key = element.getElementsByTagName("key").item(0).getTextContent();
                    String rawTranslation = element.getElementsByTagName("value").item(0).getTextContent();

                    String[] translation = rawTranslation.split(",");

                    for (int j = 0; j < translation.length; j++) {
                        translation[j] = translation[j].trim().replace("\"", "");
                    }

                    menuTranslationData.put(key, translation);
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error parsing translation file at: " + TRANSLATIONS_DIRECTORY + currentLanguage + ".xml");
            e.printStackTrace();
            return false;
        }
    }

    public String getTranslation(String key) {
        return translationData.get(key);
    }

    public String[] getMenuTranslation(String key) { return menuTranslationData.get(key); }
}
