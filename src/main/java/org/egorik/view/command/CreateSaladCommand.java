package org.egorik.view.command;

public class CreateSaladCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Create salad action!");
    }

    @Override
    public String getName() {
        return "Create salad";
    }

    @Override
    public String getDescription() {
        return "Create salad action!";
    }
}