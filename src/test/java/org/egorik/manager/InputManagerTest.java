package org.egorik.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputManagerTest {

    InputManager inputManager;
    ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }


    @Test
    void shouldReadValidInt() {
        inputManager = new InputManager(new Scanner("42"));
        assertEquals(42, inputManager.getValidInt());
    }

    @Test
    void shouldRetryUntilValidInt() {
        inputManager = new InputManager(new Scanner("abc\n12"));

        int result = inputManager.getValidInt();
        String out = outputStream.toString();

        assertEquals(12, result);
        assertTrue(out.contains("Only int"));
    }


    @Test
    void shouldReadPositiveIntImmediately() {
        inputManager = new InputManager(new Scanner("7"));
        assertEquals(7, inputManager.getPositiveInt());
    }

    @Test
    void shouldRejectNegativeInt() {
        inputManager = new InputManager(new Scanner("-5\n3"));

        int result = inputManager.getPositiveInt();
        String out = outputStream.toString();

        assertEquals(3, result);
        assertTrue(out.contains("Only positive int"));
    }


    @Test
    void shouldReadIntInRange() {
        inputManager = new InputManager(new Scanner("3"));
        assertEquals(3, inputManager.getValidIntInRange(1, 5));
    }

    @Test
    void shouldRetryIntOutOfRange() {
        inputManager = new InputManager(new Scanner("10\n3"));

        int result = inputManager.getValidIntInRange(1, 5);
        String out = outputStream.toString();

        assertEquals(3, result);
        assertTrue(out.contains("Only int in range [1;5]"));
    }


    @Test
    void shouldReturnStringAsIs() {
        inputManager = new InputManager(new Scanner("hello world"));
        assertEquals("hello world", inputManager.getString());
    }


    @Test
    void shouldAcceptNonEmptyString() {
        inputManager = new InputManager(new Scanner("text"));
        assertEquals("text", inputManager.getValidString());
    }

    @Test
    void shouldRejectEmptyString() {
        inputManager = new InputManager(new Scanner("\n\nhello"));

        String res = inputManager.getValidString();
        String out = outputStream.toString();

        assertEquals("hello", res);
        assertTrue(out.contains("String can't be empty!"));
    }


    @Test
    void shouldReturnTrueOnY() {
        inputManager = new InputManager(new Scanner("y"));
        assertTrue(inputManager.isContinue("Continue?"));
    }

    @Test
    void shouldReturnFalseOnN() {
        inputManager = new InputManager(new Scanner("n"));
        assertFalse(inputManager.isContinue("Continue?"));
    }

    @Test
    void shouldRetryUntilValidYN() {
        inputManager = new InputManager(new Scanner("abc\nmaybe\ny"));
        boolean result = inputManager.isContinue("Continue?");

        String out = outputStream.toString();

        assertTrue(result);
        assertTrue(out.contains("Only y/n"));
    }
}
