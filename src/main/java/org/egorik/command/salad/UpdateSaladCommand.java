package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.command.product.GetProductsCommand;
import org.egorik.manager.InputManager;
import org.egorik.model.Ingredient;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.model.SaladPatch;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;

import java.util.*;

public class UpdateSaladCommand implements Command {

    private final SaladService saladService;
    private final ProductService productService;
    private final InputManager inputManager;

    private SaladPatch saladPatch = new SaladPatch();

    public UpdateSaladCommand(SaladService saladService, ProductService productService, InputManager inputManager) {
        this.saladService = saladService;
        this.productService = productService;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {

        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        int ind = SelectUtility.selectInd(saladService.getAllSalads(),inputManager);
        if (ind == -1) {
            return;
        }

        Salad salad = saladService.getAllSalads().get(ind);

        System.out.println(salad);
        if (!inputManager.isContinue("Want continue to update?")) {
            return;
        }

        System.out.println(salad);
        saladPatch = new SaladPatch();

        do {
            System.out.println("""
                    1 - Name
                    2 - Ingredients
                    3 - Instructions
                    4 - Tags
                    5 - Time to finish
                    """);

            System.out.print("Enter: ");
            int userChoice = inputManager.getValidIntInRange(1, 5);

            switch (userChoice) {
                case 1 -> {
                    System.out.print("Enter new name: ");
                    saladPatch.name = inputManager.getValidString();
                }
                case 2 -> saladPatch.ingredients = updateIngredients(salad.getIngredients());
                case 3 -> saladPatch.instruction = updateInstructions(salad.getInstruction());
                case 4 -> saladPatch.tags = updateTags(salad.getTags());
                case 5 -> {
                    System.out.print("Enter new time to finish: ");
                    saladPatch.timeToFinish = inputManager.getPositiveInt();
                }
            }
        } while (inputManager.isContinue("Continue to update other values?"));

        saladService.updateSalad(ind, saladPatch);
        System.out.println("Salad updated!");
        System.out.println(salad);
    }

    private List<Ingredient> updateIngredients(List<Ingredient> ingredients) {

        List<Ingredient> list = new ArrayList<>(ingredients);

        Map<Integer, Runnable> actions = new LinkedHashMap<>();

        System.out.println("1 - Add ingredient");
        actions.put(1, () -> {
            if (productService.isProductsEmpty()) {
                System.out.println("No products available!");
                return;
            }

            new GetProductsCommand(productService).execute();
            Product product = SelectUtility.selectOne(productService.getAllProducts(),inputManager);
            if (product == null) {
                return;
            }

            System.out.print("Enter weight (g): ");
            int weight = inputManager.getPositiveInt();

            list.add(new Ingredient(product, weight));
            System.out.println("Ingredient added!");
        });

        if (!list.isEmpty()) {

            System.out.println("2 - Remove ingredient");
            actions.put(2, () -> {
                for (int i = 0; i < list.size(); i++) {
                    System.out.printf("%d). %s\n", i, list.get(i));
                }
                Ingredient selected = SelectUtility.selectOne(list,inputManager);
                if (selected != null) {
                    list.remove(selected);
                    System.out.println("Ingredient removed!");
                }
            });

            System.out.println("3 - Update ingredient weight");
            actions.put(3, () -> {
                for (int i = 0; i < list.size(); i++) {
                    System.out.printf("%d). %s\n", i, list.get(i));
                }
                Ingredient selected = SelectUtility.selectOne(list,inputManager);
                if (selected == null) {
                    return;
                }

                System.out.printf("Current weight: %.1f\n", selected.getWeight());
                System.out.println("Enter new weight (g): ");
                double newWeight = inputManager.getPositiveInt();

                list.set(list.indexOf(selected), new Ingredient(selected.getProduct(), newWeight));
            });
        }

        System.out.println("Enter: ");
        actions.get(inputManager.getValidIntInRange(1, actions.size())).run();

        return list;
    }

    private List<String> updateInstructions(List<String> original) {

        List<String> list = new ArrayList<>(original);

        Map<Integer, Runnable> actions = new LinkedHashMap<>();

        System.out.println("1 - Add step");
        actions.put(1, () -> {
            System.out.print("Enter new step: ");
            list.add(inputManager.getValidString());
            System.out.println("Added!");
        });

        if (!list.isEmpty()) {
            System.out.println("2 - Edit step");
            actions.put(2, () -> {

                for (int i = 0; i < list.size(); i++) {
                    System.out.printf("%d). %s\n", i, list.get(i));
                }

                System.out.println("Enter step number: ");
                int id = inputManager.getValidIntInRange(1, list.size());
                System.out.println("Current: " + list.get(id - 1));

                System.out.println("Enter new text: ");
                list.set(id - 1, inputManager.getValidString());
                System.out.println("Updated!");
            });

            System.out.println("3 - Remove step");
            actions.put(3, () -> {

                for (int i = 0; i < list.size(); i++) {
                    System.out.printf("%d). %s\n", i, list.get(i));
                }

                System.out.println("Enter step number: ");
                int id = inputManager.getValidIntInRange(1, list.size());
                list.remove(id - 1);
                System.out.println("Removed!");
            });

            System.out.println("4 - Clear all steps");
            actions.put(4, list::clear);
        }

        System.out.print("Enter: ");
        actions.get(inputManager.getValidIntInRange(1, actions.size())).run();

        return list;
    }

    private Set<String> updateTags(Set<String> original) {

        Set<String> tags = new HashSet<>(original);

        Map<Integer, Runnable> actions = new LinkedHashMap<>();

        System.out.println("1 - Add tag");
        actions.put(1, () -> {
            System.out.println("Enter tag: ");
            String tag = inputManager.getValidString().trim().toLowerCase();

            if (!tags.add(tag)) {
                System.out.println("Already exists!");
            }
        });

        if (!tags.isEmpty()) {
            System.out.println("2 - Remove tag");
            actions.put(2, () -> {
                System.out.println("Enter tag to remove: ");
                String tag = inputManager.getValidString().trim().toLowerCase();
                if (tags.remove(tag)) {
                    System.out.println("Removed!");
                } else {
                    System.out.println("Not found!");
                }
            });

            System.out.println("3 Clear all");
            actions.put(3, tags::clear);
        }

        System.out.print("Enter: ");
        actions.get(inputManager.getValidIntInRange(1, actions.size())).run();

        return tags;
    }

    @Override
    public String getName() {
        return "Update";
    }

    @Override
    public String getDescription() {
        return "Update salad properties";
    }
}