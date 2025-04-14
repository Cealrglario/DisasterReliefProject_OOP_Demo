package edu.ucalgary.oop;

import java.util.Scanner;

public class InputHandler {
    private final Scanner INPUT_SCANNER = new Scanner(System.in);

    public int getIntInput(int min, int max) {
        Integer userInput = null;
        boolean validInput = false;

        while (!validInput) {
            try {
                userInput = INPUT_SCANNER.nextInt();
                if (userInput >= min && userInput <= max) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please select an option between " + min + " and " + max + ": ");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please select an option by typing in its corresponding number: ");
                INPUT_SCANNER.nextLine();
            }
        }

        INPUT_SCANNER.nextLine();
        return userInput;
    }


    public String getStringInput(boolean emptyAllowed, boolean numbersAllowed) {
        String userInput = "";
        boolean validInput = false;

        while (!validInput) {
            try {
                userInput = INPUT_SCANNER.nextLine();

                if (!emptyAllowed && userInput.isEmpty()) {
                    System.out.println("Invalid input. Empty inputs are not allowed here. Try again: ");
                    continue;
                }

                if (!numbersAllowed && containsDigit(userInput)) {
                    System.out.println("Invalid input. Numbers in your input are not allowed here. Try again: ");
                    continue;
                }

                validInput = true;

            } catch (Exception e) {
                System.out.println("An error occurred while reading your input. Please try again: ");
            }
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
