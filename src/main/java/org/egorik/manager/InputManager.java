package org.egorik.manager;

import java.util.Scanner;

public class InputManager {
    private Scanner myScanner;

    public InputManager() {
        this.myScanner = new Scanner(System.in);
    }

    public InputManager(Scanner myScanner) {
        this.myScanner = myScanner;
    }

    public Scanner getMyScanner() {
        return myScanner;
    }

    public void setMyScanner(Scanner myScanner) {
        this.myScanner = myScanner;
    }

    public int getPositiveInt() {
        int result = getValidInt();
        while (result < 0) {
            System.out.print("Only positive int: ");
            result = getValidInt();
        }
        return result;
    }

    public int getValidInt() {
        while (true) {
            if (myScanner.hasNextInt()) {
                int res = myScanner.nextInt();
                if (myScanner.hasNextLine()) {
                    myScanner.nextLine(); // з’їсти \n
                }
                return res;
            } else {
                if (myScanner.hasNextLine()) {
                    myScanner.nextLine();
                }
                System.out.print("Only int: ");
            }
        }
    }


    public int getValidIntInRange(int min, int max) {
        int res = getValidInt();
        while (res > max || res < min) {
            System.out.printf("Only int in range [%d;%d]: ", min, max);
            res = getValidInt();
        }
        return res;
    }

    public String getString() {
        return myScanner.nextLine();
    }

    public String getValidString() {
        String buff = getString();
        while (buff.isBlank()) {
            System.out.println("String can't be empty!");
            buff = getString();
        }
        return buff;
    }

    public boolean isContinue(String text) {
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