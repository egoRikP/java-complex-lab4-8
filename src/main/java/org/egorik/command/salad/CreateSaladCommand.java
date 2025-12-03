package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.command.product.CreateProductCommand;
import org.egorik.manager.InputManager;
import org.egorik.model.Ingredient;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CreateSaladCommand implements Command {

    private final SaladService saladService;
    private final ProductService productService;
    private final InputManager inputManager;

    public CreateSaladCommand(SaladService saladService, ProductService productService, InputManager inputManager) {
        this.saladService = saladService;
        this.productService = productService;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {
        if (productService.isProductsEmpty()) {
            System.out.println("No products, first create product!");
            if (!inputManager.isContinue("Continue to create product and then salad?")) {
                return;
            }
            new CreateProductCommand(productService, inputManager).execute();
        }

        System.out.print("Enter salad name: ");
        String name = inputManager.getValidString();

        System.out.println("Enter ingredients: ");
        List<Ingredient> ingredients = new ArrayList<>();

        do {
            System.out.print("Enter product name: ");
            String productName = inputManager.getValidString().toLowerCase();
            List<Product> result = productService.getProductsByName(productName);

            if (result.isEmpty()) {
                System.out.println("Empty result!");
                if (!inputManager.isContinue("Continue?")) {
                    break;
                }
                continue;
            }

            int ind = SelectUtility.selectInd(result,inputManager);

            if (ind == -1) {
                continue;
            }

            System.out.print("Enter product weight: ");
            int weight = inputManager.getPositiveInt();

            ingredients.add(new Ingredient(result.get(ind), weight));

        } while (inputManager.isContinue("Add other ingredient?"));

        System.out.println("Enter instruction step by step (q - to stop):");
        List<String> instruction = new ArrayList<>();

        int step = 1;
        String buff;

        while (true) {
            System.out.printf("%d). ", step);
            buff = inputManager.getValidString();

            if (buff.equals("q")) {
                break;
            }
            step++;
            instruction.add(buff);
        }

        String stringTags;

        while (true) {
            System.out.print("Enter tags with spaces (example: \"newyear easy fast\"): ");
            stringTags = inputManager.getString().trim();

            if (!stringTags.isBlank()) {
                break;
            }

            if (stringTags.isBlank() && inputManager.isContinue("Skip tags?")) {
                break;
            }
        }

        HashSet<String> tags = new HashSet<>(List.of(stringTags.split(" ")));

        System.out.print("Enter time to finish salad: ");
        int timeToFinish = inputManager.getPositiveInt();

        saladService.addSalad(new Salad(name, timeToFinish, ingredients, instruction, tags));
    }

    @Override
    public String getName() {
        return "Create";
    }

    @Override
    public String getDescription() {
        return "Create salad";
    }
}