package org.egorik;

import org.egorik.manager.InputManager;
import org.egorik.menu.MainMenu;

public class Main {
    public static void main(String[] args) {

        System.out.println("Chef-Salad program");
        AppContext appContext = new AppContext();
        new MainMenu(appContext).menuCycle();
        InputManager.getMyScanner().close();
    }
}