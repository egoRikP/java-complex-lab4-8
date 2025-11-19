package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Product;
import org.egorik.service.ProductService;

import java.util.List;

public class GetProductsByCaloriesCommand implements Command {

    private final ProductService productService;

    public GetProductsByCaloriesCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute() {
        if (productService.isProductsEmpty()) {
            System.out.println("Product list is empty!");
            return;
        }

        System.out.println("Enter [min] calories: ");
        int min = InputManager.getPositiveInt();

        System.out.println("Enter [max] calories: ");
        int max = InputManager.getValidInt();
        while (min > max) {
            System.out.println("Bigger or equals " + min);
            max = InputManager.getValidInt();
        }

        List<Product> result = productService.getProductsByCalories(min, max);
        if (result.isEmpty()) {
            System.out.printf("Products with calories in range [%d;%d] is empty!\n", min, max);
            return;
        }

        System.out.printf("Products with calories in range [%d;%d]:\n", min, max);
        result.forEach(System.out::println);
    }

    @Override
    public String getName() {
        return "Get by calories";
    }

    @Override
    public String getDescription() {
        return "Shows products by calories in range [min;max]";
    }
}