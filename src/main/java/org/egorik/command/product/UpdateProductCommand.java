package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.*;
import org.egorik.service.ProductService;
import org.egorik.utils.SelectUtility;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateProductCommand implements Command {

    private final ProductService productService;
    private final InputManager inputManager;

    public UpdateProductCommand(ProductService productService, InputManager inputManager) {
        this.productService = productService;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {
        if (productService.isProductsEmpty()) {
            System.out.println("Product list is empty!");
            return;
        }

        int ind = SelectUtility.selectInd(productService.getAllProducts(), inputManager);

        if (ind == -1) {
            return;
        }

        Product toUpdate = productService.getAllProducts().get(ind);

        if (!inputManager.isContinue(String.format("Update %s ?", toUpdate))) {
            return;
        }

        ProductPatch productPatch = new ProductPatch();
        System.out.println(toUpdate);

        do {

            Map<Integer, Runnable> actions = new LinkedHashMap<>();

            actions.put(1, () -> {
                System.out.println("New name: ");
                productPatch.name = inputManager.getValidString();
            });
            actions.put(2, () -> {
                System.out.println("New calories: ");
                productPatch.caloriesPer100 = inputManager.getPositiveInt();
            });
            actions.put(3, () -> {
                System.out.println("New proteins: ");
                productPatch.proteinsPer100 = inputManager.getPositiveInt();
            });
            actions.put(4, () -> {
                System.out.println("New fats: ");
                productPatch.fatsPer100 = inputManager.getPositiveInt();
            });
            actions.put(5, () -> {
                System.out.println("New carbs: ");
                productPatch.carbsPer100 = inputManager.getPositiveInt();
            });

            if (toUpdate instanceof Vegetable)
                actions.put(6, () -> {
                    System.out.println("New vegetable type: ");
                    productPatch.type = inputManager.getValidString();
                });
            if (toUpdate instanceof LeafyVegetable)
                actions.put(7, () -> {
                    System.out.println("New water percentage: ");
                    productPatch.waterPercentage = inputManager.getPositiveInt();
                });
            if (toUpdate instanceof RootVegetable)
                actions.put(7, () -> {
                    System.out.println("New starch: ");
                    productPatch.starchContent = inputManager.getPositiveInt();
                });

            System.out.println("1 - Name");
            System.out.println("2 - Calories");
            System.out.println("3 - Proteins");
            System.out.println("4 - Fats");
            System.out.println("5 - Carbs");

            if (toUpdate instanceof Vegetable) {
                System.out.println("6 - Vegetable type");
            }
            if (toUpdate instanceof LeafyVegetable) {
                System.out.println("7 - Water %");
            }
            if (toUpdate instanceof RootVegetable) {
                System.out.println("7 - Starch content");
            }

            int choice = inputManager.getValidIntInRange(1, actions.size());
            actions.get(choice).run();

        } while (inputManager.isContinue("Continue to other values?"));


        productService.updateProduct(ind, productPatch);
        System.out.printf("Updated - %s", toUpdate);
    }

    @Override
    public String getName() {
        return "Update";
    }

    @Override
    public String getDescription() {
        return "Update product by ind";
    }
}