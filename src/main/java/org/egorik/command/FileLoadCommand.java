package org.egorik.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.egorik.AppContext;
import org.egorik.manager.InputManager;

public class FileLoadCommand implements Command {

    private static final Logger logger = LogManager.getLogger(FileLoadCommand.class);
    private final AppContext appContext;

    public FileLoadCommand(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void execute() {
        String productPath = appContext.fileManager.DEFAULT_PRODUCTS_PATH;
        String saladPath = appContext.fileManager.DEFAULT_SALADS_PATH;

        logger.debug("Default path to load products - {}", appContext.fileManager.DEFAULT_PRODUCTS_PATH);
        logger.debug("Default path to load salads - {}", appContext.fileManager.DEFAULT_SALADS_PATH);

        System.out.printf("Default path to load products - %s\n", appContext.fileManager.DEFAULT_PRODUCTS_PATH);
        System.out.printf("Default path to load salads - %s\n", appContext.fileManager.DEFAULT_SALADS_PATH);

        if (!InputManager.isContinue("Use default paths?")) {
            System.out.print("Enter products path: ");
            productPath = InputManager.getValidString();

            System.out.print("Enter salads path: ");
            saladPath = InputManager.getValidString();
        }

        while (true) {

            try {
                appContext.fileManager.loadProducts(productPath).forEach(appContext.productService::addProduct);
                System.out.println("Products loaded successfully!");
                break;
            } catch (Exception e) {
                logger.error("Error loading products from '{}': {}", productPath, e.getMessage(), e);
                System.out.println("Error loading products: " + e.getMessage());

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
                System.out.println("Salads loaded successfully!");
                break;
            } catch (Exception exception) {
                logger.error("Error loading salads from '{}': {}", saladPath, exception.getMessage(), exception);
                System.out.println("Error loading salads: " + exception.getMessage());
                if (!InputManager.isContinue("Try another path for salads?")) {
                    return;
                }
                System.out.print("Enter salad path: ");
                saladPath = InputManager.getValidString();
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