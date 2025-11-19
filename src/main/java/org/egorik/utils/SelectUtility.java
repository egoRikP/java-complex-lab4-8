package org.egorik.utils;

import org.egorik.manager.InputManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectUtility {

    static public <T> int selectInd(List<T> list) {

        if (list.isEmpty()) {
            System.out.println("List is empty!");
            return -1;
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d). %s\n", i, list.get(i));
        }

        System.out.print("Enter index in range [0; " + (list.size() - 1) + "] or -1 to exit: ");
        return InputManager.getValidIntInRange(-1, list.size() - 1);
    }

    static public <T> T selectOne(List<T> list) {

        if (list.isEmpty()) {
            System.out.println("List is empty!");
            return null;
        }

        System.out.print("Enter index in range [0; " + (list.size() - 1) + "] or -1 to exit: ");
        int ind = InputManager.getValidIntInRange(-1, list.size() - 1);

        if (ind == -1) {
            return null;
        }

        System.out.println(list.get(ind));

        return list.get(ind);
    }

    static public <T> List<T> selectMany(List<T> list) {
        if (list.isEmpty()) {
            System.out.println("List is empty!");
            return Collections.emptyList();
        }

        List<T> selectedList = new ArrayList<>();

        do {
            T selected = selectOne(list);

            if (selected == null) {
                break;
            }

            if (selectedList.contains(selected)) {
                System.out.println("You have already selected this!");
            } else {
                selectedList.add(selected);
            }


        } while (InputManager.isContinue("Select other?"));

        return selectedList;
    }

}