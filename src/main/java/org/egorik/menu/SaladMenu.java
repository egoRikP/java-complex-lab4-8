package org.egorik.menu;

import org.egorik.AppContext;
import org.egorik.command.salad.*;

public class SaladMenu extends AbstractMenu {

    public SaladMenu(AppContext appContext) {
        super(appContext);
    }

    @Override
    protected void init() {
        addCommand(1, new CreateSaladCommand(appContext.saladService, appContext.productService));
        addCommand(2, new GetSaladsCommand(appContext.saladService));
        addCommand(3, new GetMoreInfoAboutSaladCommand(appContext.saladService));
        addCommand(4, new GetSaladsByNameCommand(appContext.saladService));
        addCommand(5, new GetSaladsByCaloriesCommand(appContext.saladService));
        addCommand(6, new GetSaladsByIngredientsCommand(appContext.saladService, appContext.productService));
        addCommand(7, new GetSaladsByTagSetCommand(appContext.saladService));
        addCommand(8, new DeleteSaladCommand(appContext.saladService));
        addCommand(9, new UpdateSaladCommand(appContext.saladService, appContext.productService));
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
