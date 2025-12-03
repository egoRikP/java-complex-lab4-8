package org.egorik.command.product;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.LeafyVegetable;
import org.egorik.model.Product;
import org.egorik.model.RootVegetable;
import org.egorik.model.Vegetable;
import org.egorik.service.ProductService;

public class CreateProductCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CreateProductCommand.class);
    private final ProductService productService;
    private final InputManager inputManager;

    public CreateProductCommand(ProductService productService, InputManager inputManager) {
        this.productService = productService;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {
        System.out.println("Product type:");
        System.out.println("1 - Normal product");
        System.out.println("2 - Vegetable");
        System.out.println("3 - Leafy vegetable");
        System.out.println("4 - Root vegetable");
        int type = inputManager.getValidIntInRange(1, 4);

        System.out.print("Enter product name: ");
        String name = inputManager.getValidString();

        System.out.print("Enter calories per 100g: ");
        int calories = inputManager.getPositiveInt();

        System.out.print("Enter proteins per 100g: ");
        int proteins = inputManager.getPositiveInt();

        System.out.print("Enter fats per 100g: ");
        int fats = inputManager.getPositiveInt();

        System.out.print("Enter carbs per 100g: ");
        int carbs = inputManager.getPositiveInt();

        Product newProduct;

        if (type == 1) {
            newProduct = new Product(name, calories, proteins, fats, carbs);
        } else {
            System.out.print("Enter vegetable type: ");
            String vegType = inputManager.getValidString();
            newProduct = switch (type) {
                case 2 -> new Vegetable(name, calories, proteins, fats, carbs, vegType);
                case 3 -> {
                    System.out.print("Enter water percentage: ");
                    int water = inputManager.getPositiveInt();
                    yield new LeafyVegetable(name, calories, proteins, fats, carbs, vegType, water);
                }
                case 4 -> {
                    System.out.print("Enter starch content: ");
                    int starch = inputManager.getPositiveInt();
                    yield new RootVegetable(name, calories, proteins, fats, carbs, vegType, starch);
                }
                default -> throw new IllegalArgumentException("Invalid type" + type);
            };
        }

        if (!productService.isProductExists(newProduct)) {
            productService.addProduct(newProduct);
            System.out.println("Added new product: " + newProduct);
        } else {
            logger.warn("Product - {} - exists", newProduct);
            System.out.printf("Product - %s - exists!\n", newProduct);
        }
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