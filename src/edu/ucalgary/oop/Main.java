package edu.ucalgary.oop;

import java.util.Scanner;

public class Main {
    public static void Main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        LanguageManager languageManager = LanguageManager.INSTANCE;
        MenuManager menuManager = MenuManager.INSTANCE;

        System.out.println("Please select a language (for example, type en-CA for Canadian English): ");

        languageManager.setCurrentLanguage(userInput.nextLine());

        System.out.println("The language you selected is: " + languageManager.getCurrentLanguage());

        menuManager.initializeMenuLanguage();
        menuManager.startRunning();

        while(menuManager.getIsRunning()) {
            menuManager.run();
        }
    }
}
