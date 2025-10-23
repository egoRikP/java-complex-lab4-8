package org.egorik.view.menu;

import org.egorik.InputManager;
import org.egorik.view.command.Command;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMenu implements Command {

    private final Map<Integer, Command> commands = new HashMap<>();

    public AbstractMenu() {
        init();
    }

    protected abstract void init();

    //-1 reversed for exit, 0 - help
    public void addCommand(int id, Command command) {
        if (id == -1 || id == 0) {
            throw new IllegalArgumentException("These keys are default! -1 for exit, 0 - for help! Select other!");
        }

        if (commands.containsKey(id)) {
            throw new IllegalArgumentException("Command with id " + id + " already exists!");
        }

        commands.put(id, command);
    }

    //withHelp = true -> menu with help (show description for menu and every action)
    public void show(boolean withHelp) {
        if (commands.isEmpty()) {
            System.out.println("Menu is empty!");
            return;
        }

        System.out.printf("===== Menu %s ====%s\n", getName(), withHelp ? String.format("\n%s", getDescription()) : "");

        for (Map.Entry<Integer, Command> a : commands.entrySet()) {
            System.out.printf("%d) %s %s\n",
                    a.getKey(),
                    a.getValue().getName(),
                    withHelp ? " - " + a.getValue().getDescription() : ""
            );
        }

        System.out.println("-1) - Exit menu");
        System.out.println(" 0) - Help");
        System.out.printf("===== Menu %s ====\n", getName());

    }

    public void use(int id) {
        if (commands.isEmpty() || !commands.containsKey(id)) {
            return;
        }
        commands.get(id).execute();
    }

    //-1 for exit menu, 0 for help
    public void menuCycle() {
        if (commands.isEmpty()) {
            System.out.println("Menu is empty!");
            return;
        }

        int userInput = -1;
        while (true) {
            if (userInput != 0) {
                show(false);
            }

            System.out.println();
            System.out.print("Enter action id: ");
            userInput = InputManager.getValidInt();

            switch (userInput) {
                case -1 -> {
                    System.out.println("Exit menu!");
                    return;
                }
                case 0 -> {
                    show(true);
                    continue;
                }
            }

            if (!commands.containsKey(userInput)) {
                System.out.println("No action with this id! Try again: ");
            } else {
                use(userInput);
            }

            System.out.println();
        }

    }

    @Override
    public void execute() {
        menuCycle();
    }
}