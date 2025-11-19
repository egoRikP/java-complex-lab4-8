package org.egorik.command;

import org.egorik.AppContext;
import org.egorik.manager.InputManager;

import java.io.FileNotFoundException;

public class FileLoadCommand implements Command {

    private final AppContext appContext;

    public FileLoadCommand(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void execute() {
        System.out.println("Loading products and salads!");

        System.out.printf("Default path to load products - %s\n", appContext.fileManager.DEFAULT_PRODUCTS_PATH);
        System.out.printf("Default path to load salads - %s\n", appContext.fileManager.DEFAULT_SALADS_PATH);

        if (InputManager.isContinue("Use default paths?")) {
            try {
                appContext.fileManager.loadProducts(appContext.fileManager.DEFAULT_PRODUCTS_PATH).forEach(appContext.productService::addProduct);

                System.out.println("Products loaded!");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println(fileNotFoundException.getMessage());
            }

            try {
                appContext.fileManager.loadSalads(appContext.fileManager.DEFAULT_SALADS_PATH, appContext.productService.getAllProducts()).forEach(appContext.saladService::addSalad);

                System.out.println("Salads loaded!");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println(fileNotFoundException.getMessage());
            }
        } else {

            System.out.print("Enter products path: ");
            String productPath = InputManager.getValidString();

            System.out.print("Enter salads path: ");
            String saladPath = InputManager.getValidString();

            while (true) {

                try {
                    appContext.fileManager.loadProducts(productPath).forEach(appContext.productService::addProduct);
                    System.out.println("Products loaded!");
                    break;
                } catch (FileNotFoundException fileNotFoundException) {
                    System.out.println(fileNotFoundException.getMessage());
                    if (!InputManager.isContinue("Try another path for products?")) {
                        return;
                    }
                    System.out.print("Enter products path: ");
                    productPath = InputManager.getValidString();
                }
            }

            while (true) {
                try {
                    appContext.fileManager.loadSalads(saladPath, appContext.productService.getAllProducts()).forEach(appContext.saladService::addSalad);
                    System.out.println("Salads loaded!");
                    break;
                } catch (FileNotFoundException fileNotFoundException) {
                    System.out.println(fileNotFoundException.getMessage());
                    if (!InputManager.isContinue("Try another path for salads?")) {
                        return;
                    }
                    System.out.print("Enter salad path: ");
                    saladPath = InputManager.getValidString();
                }

            }
        }

    }

    @Override
    public String getName() {
        return "Load";
    }

    @Override
    public String getDescription() {
        return "Load salads and products from files";
    }
}