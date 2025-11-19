package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;

import java.util.List;

public class DeleteProductCommand implements Command {

    private final SaladService saladService;
    private final ProductService productService;

    public DeleteProductCommand(ProductService productService, SaladService saladService) {
        this.productService = productService;
        this.saladService = saladService;
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

        Product product = productService.getAllProducts().get(ind);

        List<Salad> saladList = saladService.getSaladsByProducts(product);

        if (saladList.isEmpty()) {
            if (InputManager.isContinue(String.format("Delete %s ?", productService.getAllProducts().get(ind)))) {
                productService.deleteProduct(ind);
            }
        } else {
            System.out.println("Can't delete product! Because he is in salads!");
            saladList.forEach(System.out::println);
        }

    }

    @Override
    public String getName() {
        return "Delete";
    }

    @Override
    public String getDescription() {
        return "Delete product by ind";
    }
}