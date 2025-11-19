package org.egorik.command;

import org.egorik.AppContext;
import org.egorik.manager.InputManager;

public class FileSaveCommand implements Command {

    private final AppContext appContext;

    public FileSaveCommand(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void execute() {
        System.out.println("Saving products and salads!");

        System.out.printf("Default path to save products - %s\n", appContext.fileManager.DEFAULT_PRODUCTS_PATH);
        System.out.printf("Default path to save salads - %s\n", appContext.fileManager.DEFAULT_SALADS_PATH);

        if (InputManager.isContinue("Use default paths?")) {
            appContext.fileManager.saveProducts(appContext.productService.getAllProducts(), appContext.fileManager.DEFAULT_PRODUCTS_PATH);
            appContext.fileManager.saveSalads(appContext.saladService.getAllSalads(), appContext.fileManager.DEFAULT_SALADS_PATH);
        } else {

            System.out.print("Enter products path: ");
            String productPath = InputManager.getValidString();

            System.out.print("Enter salads path: ");
            String saladPath = InputManager.getValidString();

            appContext.fileManager.saveProducts(appContext.productService.getAllProducts(), productPath);
            appContext.fileManager.saveSalads(appContext.saladService.getAllSalads(), saladPath);
        }

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