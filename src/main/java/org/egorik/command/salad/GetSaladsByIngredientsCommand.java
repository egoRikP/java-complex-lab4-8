package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.command.product.GetProductsCommand;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;

import java.util.List;

public class GetSaladsByIngredientsCommand implements Command {

    private final SaladService saladService;
    private final ProductService productService;

    public GetSaladsByIngredientsCommand(SaladService saladService, ProductService productService) {
        this.saladService = saladService;
        this.productService = productService;
    }

    @Override
    public void execute() {

        if (saladService.isSaladsEmpty()) {
            System.out.println("Salad list is empty!");
            return;
        }

        if (productService.isProductsEmpty()) {
            System.out.println("Product list is empty!");
            return;
        }

        System.out.print("Enter ingredients: ");
        new GetProductsCommand(productService).execute();

        List<Product> ingredients = SelectUtility.selectMany(productService.getAllProducts());
        List<Salad> result = saladService.getSaladsByProducts(ingredients);

        if (result.isEmpty()) {
            System.out.println("Can't find any salads with this products:");
            ingredients.forEach(System.out::println);
            return;
        }

        System.out.println("Salads with products: ");
        result.forEach(System.out::println);
    }

    @Override
    public String getName() {
        return "By products";
    }

    @Override
    public String getDescription() {
        return "Get all salads by products";
    }
}