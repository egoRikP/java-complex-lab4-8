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

    public UpdateProductCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute() {
        if (productService.isProductsEmpty()) {
            System.out.println("Product list is empty!");
            return;
        }

        int ind = SelectUtility.selectInd(productService.getAllProducts());

        if (ind == -1) {
            return;
        }

        Product toUpdate = productService.getAllProducts().get(ind);

        if (!InputManager.isContinue(String.format("Update %s ?", toUpdate))) {
            return;
        }

        ProductPatch productPatch = new ProductPatch();
        System.out.println(toUpdate);

        do {

            Map<Integer, Runnable> actions = new LinkedHashMap<>();

            actions.put(1, () -> {
                System.out.println("New name: ");
                productPatch.name = InputManager.getValidString();
            });
            actions.put(2, () -> {
                System.out.println("New calories: ");
                productPatch.caloriesPer100 = InputManager.getPositiveInt();
            });
            actions.put(3, () -> {
                System.out.println("New proteins: ");
                productPatch.proteinsPer100 = InputManager.getPositiveInt();
            });
            actions.put(4, () -> {
                System.out.println("New fats: ");
                productPatch.fatsPer100 = InputManager.getPositiveInt();
            });
            actions.put(5, () -> {
                System.out.println("New carbs: ");
                productPatch.carbsPer100 = InputManager.getPositiveInt();
            });

            if (toUpdate instanceof Vegetable)
                actions.put(6, () -> {
                    System.out.println("New vegetable type: ");
                    productPatch.type = InputManager.getValidString();
                });
            if (toUpdate instanceof LeafyVegetable)
                actions.put(7, () -> {
                    System.out.println("New water percentage: ");
                    productPatch.waterPercentage = InputManager.getPositiveInt();
                });
            if (toUpdate instanceof RootVegetable)
                actions.put(7, () -> {
                    System.out.println("New starch: ");
                    productPatch.starchContent = InputManager.getPositiveInt();
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

            int choice = InputManager.getValidIntInRange(1, actions.size());
            actions.get(choice).run();

        } while (InputManager.isContinue("Continue to other values?"));


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