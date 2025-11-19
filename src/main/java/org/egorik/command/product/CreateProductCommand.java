package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.LeafyVegetable;
import org.egorik.model.Product;
import org.egorik.model.RootVegetable;
import org.egorik.model.Vegetable;
import org.egorik.service.ProductService;

public class CreateProductCommand implements Command {

    private final ProductService productService;

    public CreateProductCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute() {
        System.out.println("Product type:");
        System.out.println("1 - Normal product");
        System.out.println("2 - Vegetable");
        System.out.println("3 - Leafy vegetable");
        System.out.println("4 - Root vegetable");
        int type = InputManager.getValidIntInRange(1, 4);

        System.out.print("Enter product name: ");
        String name = InputManager.getValidString();

        System.out.print("Enter calories per 100g: ");
        int calories = InputManager.getPositiveInt();

        System.out.print("Enter proteins per 100g: ");
        int proteins = InputManager.getPositiveInt();

        System.out.print("Enter fats per 100g: ");
        int fats = InputManager.getPositiveInt();

        System.out.print("Enter carbs per 100g: ");
        int carbs = InputManager.getPositiveInt();

        Product newProduct;

        if (type == 1) {
            newProduct = new Product(name, calories, proteins, fats, carbs);
        } else {
            System.out.print("Enter vegetable type: ");
            String vegType = InputManager.getValidString();
            newProduct = switch (type) {
                case 2 -> new Vegetable(name, calories, proteins, fats, carbs, vegType);
                case 3 -> {
                    System.out.print("Enter water percentage: ");
                    int water = InputManager.getPositiveInt();
                    yield new LeafyVegetable(name, calories, proteins, fats, carbs, vegType, water);
                }
                case 4 -> {
                    System.out.print("Enter starch content: ");
                    int starch = InputManager.getPositiveInt();
                    yield new RootVegetable(name, calories, proteins, fats, carbs, vegType, starch);
                }
                default -> throw new IllegalArgumentException("Invalid type" + type);
            };
        }

        productService.addProduct(newProduct);
        System.out.println("Added new product: " + newProduct);
    }


    @Override
    public String getName() {
        return "Create";
    }

    @Override
    public String getDescription() {
        return "Create product (name, calories, proteins, fats and carbs per 100 gram)";
    }
}