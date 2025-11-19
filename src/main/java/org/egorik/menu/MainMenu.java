package org.egorik.menu;

import org.egorik.AppContext;

public class MainMenu extends AbstractMenu {

    public MainMenu(AppContext appContext) {
        super(appContext);
    }

    @Override
    protected void init() {
        addCommand(1, new ProductMenu(appContext));
        addCommand(2, new SaladMenu(appContext));
        addCommand(3, new FileMenu(appContext));
    }

    @Override
    public String getName() {
        return "Main menu";
    }

    @Override
    public String getDescription() {
        return "Includes all subMenus to work with Products, Salads, Files";
    }
}