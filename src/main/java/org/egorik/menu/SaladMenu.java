package org.egorik.menu;

import org.egorik.AppContext;
import org.egorik.command.salad.*;

public class SaladMenu extends AbstractMenu {

    public SaladMenu(AppContext appContext) {
        super(appContext);
    }

    @Override
    protected void init() {
        addCommand(1, new CreateSaladCommand(appContext.saladService, appContext.productService, appContext.inputManager));
        addCommand(2, new GetSaladsCommand(appContext.saladService));
        addCommand(3, new GetMoreInfoAboutSaladCommand(appContext.saladService,appContext.inputManager));
        addCommand(4, new GetSaladsByNameCommand(appContext.saladService, appContext.inputManager));
        addCommand(5, new GetSaladsByCaloriesCommand(appContext.saladService, appContext.inputManager));
        addCommand(6, new GetSaladsByIngredientsCommand(appContext.saladService, appContext.productService,appContext.inputManager));
        addCommand(7, new GetSaladsByTagSetCommand(appContext.saladService, appContext.inputManager));
        addCommand(8, new DeleteSaladCommand(appContext.saladService, appContext.inputManager));
        addCommand(9, new UpdateSaladCommand(appContext.saladService, appContext.productService, appContext.inputManager));
    }

    @Override
    public String getName() {
        return "Salad menu";
    }

    @Override
    public String getDescription() {
        return "Create, get, update, delete salads";
    }
}
