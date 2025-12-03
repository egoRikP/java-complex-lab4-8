package org.egorik.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.egorik.AppContext;
import org.egorik.manager.InputManager;

public class FileSaveCommand implements Command {
    private static final Logger logger = LogManager.getLogger(FileSaveCommand.class);

    private final AppContext appContext;

    private final InputManager inputManager;

    public FileSaveCommand(AppContext appContext, InputManager inputManager) {
        this.appContext = appContext;
        this.inputManager = inputManager;
    }

    @Override
    public void execute() {
        System.out.println("Saving products and salads!");

        String productPath = appContext.fileManager.DEFAULT_PRODUCTS_PATH;
        String saladPath = appContext.fileManager.DEFAULT_SALADS_PATH;

        System.out.printf("Default path to save products - %s\n", productPath);
        System.out.printf("Default path to save salads - %s\n", saladPath);

        if (!inputManager.isContinue("Use default paths?")) {
            System.out.print("Enter products path: ");
            productPath = inputManager.getValidString();

            System.out.print("Enter salads path: ");
            saladPath = inputManager.getValidString();
        }

        appContext.fileManager.saveProducts(appContext.productService.getAllProducts(), productPath);
        System.out.println("Products saved successfully!");

        appContext.fileManager.saveSalads(appContext.saladService.getAllSalads(), saladPath);
        System.out.println("Salads saved successfully!");

    }

    @Override
    public String getName() {
        return "Save";
    }

    @Override
    public String getDescription() {
        return "Saves products and salads to file";
    }
}