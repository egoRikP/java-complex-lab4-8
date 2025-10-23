package org.egorik;

import java.util.Scanner;

public class InputManager {
    private static final Scanner myScanner = new Scanner(System.in);

    private InputManager() {
    }

    public static Scanner getMyScanner() {
        return myScanner;
    }

    public static int getValidInt() {

        int res;

        while (true) {

            if (myScanner.hasNextInt()) {
                res = myScanner.nextInt();
                myScanner.nextLine();
                break;
            } else {
                System.out.println("Only int!");
                myScanner.next();
            }

        }
        return res;
    }
}