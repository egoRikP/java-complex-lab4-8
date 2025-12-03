package org.egorik.menu;

import org.egorik.AppContext;
import org.egorik.command.product.*;

public class ProductMenu extends AbstractMenu {

    public ProductMenu(AppContext appContext) {
        super(appContext);
    }

    @Override
    protected void init() {
        addCommand(1, new CreateProductCommand(appContext.productService, appContext.inputManager));
        addCommand(2, new GetProductsCommand(appContext.productService));
        addCommand(3, new GetProductsByCaloriesCommand(appContext.productService, appContext.inputManager));
        addCommand(4, new GetProductsByNameCommand(appContext.productService, appContext.inputManager));
        addCommand(5, new SortProductByCaloriesCommand(appContext.productService,appContext.inputManager));
        addCommand(6, new UpdateProductCommand(appContext.productService,appContext.inputManager));
        addCommand(7, new DeleteProductCommand(appContext.productService, appContext.saladService, appContext.inputManager));
    }

    @Override
    public String getName() {
        return "Product menu";
    }

    @Override
    public String getDescription() {
        return "Create, get, update, delete products";
    }
}
