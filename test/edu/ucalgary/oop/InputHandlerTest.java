package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class InputHandlerTest {
    private final InputStream systemIn = System.in;
    private InputHandler inputHandler;

    @After
    public void tearDown() {
        System.setIn(systemIn);
    }

    @Test
    public void testGetIntInput() {
        String input = "2";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        inputHandler = new InputHandler();

        int result = inputHandler.getIntInput(1, 4);

        assertEquals("getIntInput() should simply return the inputted int value if it's within range", 2, result);
    }

    @Test
    public void testGetInvalidIntInput() {
        String input = "-2\n2";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        inputHandler = new InputHandler();

        int result = inputHandler.getIntInput(1, 4);

        assertEquals("getIntInput() should request a new input if it's not within range", 2, result);
    }

    @Test
    public void testGetIntInputWithNonInteger() {
        String input = "abc\n2";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        inputHandler = new InputHandler();

        int result = inputHandler.getIntInput(1, 4);

        assertEquals("getIntInput() should request a new input if it's not an integer", 2, result);
    }

    @Test
    public void testGetStringInput() {
        String input = "Test";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        inputHandler = new InputHandler();

        String result = inputHandler.getStringInput(false, false);

        assertEquals("getStringInput() should simply return the inputted string if it adheres to requirements", "Test", result);
    }

    @Test
    public void testGetEmptyStringInputNotAllowed() {
        String input = "\nTest";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        inputHandler = new InputHandler();

        String result = inputHandler.getStringInput(false, false);

        assertEquals("getStringInput() should request a new input if it detects a forbidden empty input", "Test", result);
    }

    @Test
    public void testGetStringInputWithNumbersNotAllowed() {
        String input = "Test123\nTest";
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        inputHandler = new InputHandler();

        String result = inputHandler.getStringInput(false, false);

        assertEquals("getStringInput() should request a new input if it detects numbers in the input when forbidden", "Test", result);
    }

    @Test
    public void testContainsDigitTrue() {
        boolean result = InputHandler.containsDigit("Test123");

        assertTrue("containsDigit() should return true for a string with digits", result);
    }

    @Test
    public void testContainsDigitFalse() {
        boolean result = InputHandler.containsDigit("Test");

        assertFalse("containsDigit() should return false for a string without digits", result);
    }

    @Test
    public void testGetInputScanner() {
        inputHandler = new InputHandler();

        Scanner scanner = inputHandler.getInputScanner();

        assertNotNull("getInputScanner() should return a non-null Scanner object", scanner);
    }
}
