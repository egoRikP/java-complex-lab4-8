package org.egorik.menu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.egorik.AppContext;
import org.egorik.command.Command;
import org.egorik.manager.InputManager;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMenu implements Command {

    private static final Logger logger = LogManager.getLogger(AbstractMenu.class);
    private final Map<Integer, Command> commands = new HashMap<>();
    protected AppContext appContext;

    public AbstractMenu(AppContext appContext) {
        this.appContext = appContext;
        init();
    }

    protected abstract void init();

    //-1 reversed for exit, 0 - help
    public void addCommand(int id, Command command) {
        if (id == -1 || id == 0) {
            logger.error("These keys are default for all menus! -1 for exit, 0 - for help! Select other!");
            throw new IllegalArgumentException("These keys are default for all menus! -1 for exit, 0 - for help! Select other!");
        }

        if (commands.containsKey(id)) {
            logger.error("Command with id {} already exists in {}!", id, getName());
            throw new IllegalArgumentException("Command with id " + id + " already exists in " + getName());
        }

        logger.debug("Add {} command to {}", command.getName(), getName());
        commands.put(id, command);
    }

    //withHelp = true -> menu with help (show description for menu and every action)
    public void show(boolean withHelp) {
        if (commands.isEmpty()) {
            logger.warn("Menu {} is empty!", getName());
            System.out.printf("Menu %s is empty!\n", getName());
            return;
        }

        System.out.printf("===== %s ====%s\n", getName(), withHelp ? String.format("\n%s", getDescription()) : "");

        for (Map.Entry<Integer, Command> a : commands.entrySet()) {
            System.out.printf("%d) %s %s\n",
                    a.getKey(),
                    a.getValue().getName(),
                    withHelp ? " - " + a.getValue().getDescription() : ""
            );
        }

//        System.out.println("0 - Help");
//        System.out.println("-1 - Exit menu");
        System.out.printf("===== %s ====\n", getName());

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
            System.out.printf("Menu %s is empty!\n", getName());
            return;
        }

        int userInput = -1;
        while (true) {
            if (userInput != 0) {
                show(false);
            }

            System.out.println();
            System.out.print("Enter action id: ");
            userInput = InputManager.getValidIntInRange(-1, commands.size());

            switch (userInput) {
                case -1 -> {
                    logger.info("Exit from menu '{}'", getName());
                    System.out.printf("Exit %s!", getName());
                    return;
                }
                case 0 -> {
                    logger.info("Show help for menu '{}'", getName());
                    show(true);
                    continue;
                }
            }

            if (!commands.containsKey(userInput)) {
                System.out.println("No action with this id! Try again: ");
                logger.warn("No action with id {}!", userInput);
            } else {
                logger.info("Start command - {}, {}", commands.get(userInput).getName(), commands.get(userInput).getDescription());
                use(userInput);
                logger.info("End command - {}, {}", commands.get(userInput).getName(), commands.get(userInput).getDescription());
            }

            System.out.println();
        }

    }

    @Override
    public void execute() {
        menuCycle();
    }
}