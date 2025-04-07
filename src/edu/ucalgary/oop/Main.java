package edu.ucalgary.oop;

import java.util.Scanner;

public class Main {
    public static void Main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        String selectedLanguage;
        LanguageManager languageManager = LanguageManager.INSTANCE;
        MenuManager menuManager = MenuManager.INSTANCE;

        System.out.println("Please select a language (for example, type en-CA for Canadian English): ");
        selectedLanguage = userInput.nextLine();

        if(!languageManager.configureLanguage(selectedLanguage)) {
            System.exit(1);
        }

        userInput.close();

        menuManager.initializeMenuLanguage();
        menuManager.startRunning();

        while(menuManager.getIsRunning()) {
            menuManager.run();
        }
    }
}
