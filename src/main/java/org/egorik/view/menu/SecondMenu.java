package org.egorik.view.menu;

import org.egorik.view.command.CreateSaladCommand;

public class SecondMenu extends AbstractMenu {

    @Override
    protected void init() {
        addCommand(1, new CreateSaladCommand());
    }

    @Override
    public String getName() {
        return "SecondMenu";
    }

    @Override
    public String getDescription() {
        return "SecondMenu some description...";
    }
}