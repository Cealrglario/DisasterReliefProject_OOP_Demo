package edu.ucalgary.oop;

import java.io.InputStream;
import java.util.Scanner;

public class InputHandler {
    private final Scanner INPUT_SCANNER = new Scanner(System.in);

    public int getIntInput(int min, int max) {
        return 0;
    }

    public String getStringInput(boolean emptyAllowed, boolean numbersAllowed) {
        return "";
    }

    public Scanner getInputScanner() {
        return this.INPUT_SCANNER;
    }
}
