package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.service.ProductService;

public class SortProductByCaloriesCommand implements Command {

    private final ProductService productService;

    public SortProductByCaloriesCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute() {
        if (productService.isProductsEmpty()) {
            System.out.println("Product list is empty!");
            return;
        }

        new GetProductsCommand(productService).execute();

        if (InputManager.isContinue("Sort?")) {
            boolean choice = InputManager.isContinue("In asc?");
            productService.sortProductsByCalories(choice);
        }

        new GetProductsCommand(productService).execute();
    }

    @Override
    public String getName() {
        return "Sort by calories";
    }

    @Override
    public String getDescription() {
        return "Sort by calories in asc/desc";
    }
}