package org.egorik.view.menu;

import org.egorik.view.command.CreateSaladCommand;

public class MainMenu extends AbstractMenu {

    @Override
    protected void init() {
        addCommand(1, new CreateSaladCommand());
        addCommand(2, new SecondMenu());
    }

    @Override
    public String getName() {
        return "MainMenu";
    }

    @Override
    public String getDescription() {
        return "MainMenu some description...";
    }
}