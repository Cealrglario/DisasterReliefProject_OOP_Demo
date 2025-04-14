package edu.ucalgary.oop;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        String selectedLanguage;
        LanguageManager languageManager = LanguageManager.INSTANCE;
        MenuManager menuManager = MenuManager.INSTANCE;
        ErrorLogger errorLogger = ErrorLogger.INSTANCE;

        try {
            System.out.println("Please select a language (for example, type en-CA for Canadian English): ");
            selectedLanguage = userInput.nextLine();

            if(!languageManager.configureLanguage(selectedLanguage)) {
                System.exit(1);
            }

            menuManager.startRunning();

            while(menuManager.getIsRunning()) {
                menuManager.run();
            }

            userInput.close();
            System.exit(1);
        } catch (Exception e) {
            errorLogger.logError(e);

            System.out.println("Fatal exception caught - logging error and exiting program...");
            System.out.println("---------------------------------------------------------------------");
            System.exit(1);
        }
    }
}
