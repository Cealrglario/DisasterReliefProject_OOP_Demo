package edu.ucalgary.oop;

import java.util.Scanner;

public class InputHandler {
    private final Scanner INPUT_SCANNER = new Scanner(System.in);

    public int getIntInput(int min, int max) {
        Integer userInput = INPUT_SCANNER.nextInt();
        boolean intInput = false;

        while(!intInput) {
            try {
                userInput = INPUT_SCANNER.nextInt();
                intInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please select an option by typing in its corresponding number: ");
                userInput = INPUT_SCANNER.nextInt();
            }
        }

        while(userInput < min || userInput > max) {
            System.out.println("Invalid input. Please select an option by typing in its corresponding number: ");
            userInput = INPUT_SCANNER.nextInt();
        }

        return userInput;
    }

    public String getStringInput(boolean emptyAllowed, boolean numbersAllowed) {
        String userInput = INPUT_SCANNER.nextLine();
        boolean validInput = false;

        while(!validInput) {
            while(!emptyAllowed && userInput.isEmpty()) {
                System.out.println("Invalid input. Empty inputs are not allowed here. Try again: ");
                userInput = INPUT_SCANNER.nextLine();
            }

            while(!numbersAllowed && containsDigit(userInput)) {
                System.out.println("Invalid input. Numbers in your input are not allowed here. Try again: ");
                userInput = INPUT_SCANNER.nextLine();
            }

            validInput = true;
        }

        return userInput;
    }

    public static boolean containsDigit(String str) {
        return str.matches(".*\\d+.*");
    }

    public Scanner getInputScanner() {
        return this.INPUT_SCANNER;
    }
}
