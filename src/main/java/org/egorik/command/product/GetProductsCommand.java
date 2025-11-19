package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.model.Product;
import org.egorik.service.ProductService;

import java.util.List;


public class GetProductsCommand implements Command {

    private final ProductService productService;

    public GetProductsCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void execute() {
        if (productService.isProductsEmpty()) {
            System.out.println("Product list is empty!");
            return;
        }

        System.out.println("All products:");
        List<Product> productList = productService.getAllProducts();
        for (int i = 0; i < productList.size(); i++) {
            System.out.printf("%d). %s\n", i, productList.get(i));
        }
    }

    @Override
    public String getName() {
        return "Get all";
    }

    @Override
    public String getDescription() {
        return "Show all products";
    }
}