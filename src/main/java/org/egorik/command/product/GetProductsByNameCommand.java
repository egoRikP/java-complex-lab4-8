package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Product;
import org.egorik.service.ProductService;

import java.util.List;

public class GetProductsByNameCommand implements Command {

    private final ProductService productService;
    private final InputManager inputManager;

    public GetProductsByNameCommand(ProductService productService, InputManager inputManager) {
        this.productService = productService;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {
        if (productService.isProductsEmpty()) {
            System.out.println("Product list is empty!");
            return;
        }

        System.out.println("Enter product name to find: ");
        String name = inputManager.getValidString().toLowerCase();

        List<Product> result = productService.getProductsByName(name);

        if (result.isEmpty()) {
            System.out.printf("Products with name %s is empty!\n", name);
            return;
        }

        System.out.printf("Products with name %s:\n", name);
        result.forEach(System.out::println);
    }

    @Override
    public String getName() {
        return "By name";
    }

    @Override
    public String getDescription() {
        return "Show products by name";
    }
}