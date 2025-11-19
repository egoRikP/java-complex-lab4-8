package org.egorik.manager;

import java.util.Scanner;

public class InputManager {
    private static final Scanner myScanner = new Scanner(System.in);

    public static Scanner getMyScanner() {
        return myScanner;
    }

    private InputManager() {
    }

    static public int getPositiveInt() {
        int result = getValidInt();
        while (result < 0) {
            System.out.print("Only positive int: ");
            result = getValidInt();
        }
        return result;
    }

    static public int getValidInt() {
        while (true) {
            if (myScanner.hasNextInt()) {
                int res = myScanner.nextInt();
                myScanner.nextLine();
                return res;
            } else {
                myScanner.nextLine();
                System.out.print("Only int: ");
            }
        }
    }

    static public int getValidIntInRange(int min, int max) {
        int res = getValidInt();
        while (res > max || res < min) {
            System.out.printf("Only int in range [%d;%d]: ", min, max);
            res = getValidInt();
        }
        return res;
    }

    public static String getString() {
        return myScanner.nextLine();
    }

    public static String getValidString() {
        String buff = getString();
        while (buff.isBlank()) {
            System.out.println("String can't be empty!");
            buff = getString();
        }
        return buff;
    }

    public static boolean isContinue(String text) {
        System.out.println(text + " (y/n)");

        while (true) {
            String buff = myScanner.nextLine().trim().toLowerCase();
            if (buff.equals("y")) {
                return true;
            }
            if (buff.equals("n")) {
                return false;
            }
            System.out.println("Only y/n:");
        }
    }
}