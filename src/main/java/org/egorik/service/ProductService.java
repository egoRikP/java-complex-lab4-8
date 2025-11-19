package org.egorik.service;

import org.egorik.model.Product;
import org.egorik.model.ProductPatch;
import org.egorik.repository.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ProductService {

    private final Repository<Product> repository;

    public ProductService(Repository<Product> repository) {
        this.repository = repository;
    }

    public boolean isProductsEmpty() {
        return repository.isEmpty();
    }

    public boolean isProductExists(Product product) {
        return getAllProducts().contains(product);
    }

    public void addProduct(Product newProduct) {
        if (isProductExists(newProduct)) {
            System.out.printf("Product - %s - exists!\n", newProduct);
            return;
        }
        repository.add(newProduct);
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(repository.getAll());
    }

    public void updateProduct(int ind, ProductPatch patch) {
        patch.updateProduct(repository.getAll().get(ind));
    }

    public void deleteProduct(int ind) {
        repository.delete(ind);
    }

    private List<Product> filter(Predicate<Product> predicate) {
        return repository.getAll().stream().filter(predicate).toList();
    }

    public List<Product> getProductsByName(String name) {
        return filter(product -> product.getName().toLowerCase().contains(name.trim().toLowerCase()));
    }

    public List<Product> getProductsByCalories(int minCalories, int maxCalories) {
        return filter(product -> {
            int calories = product.getCaloriesPer100();
            return calories >= minCalories && calories <= maxCalories;
        });
    }

    public void sortProductsByCalories(boolean inAscending) {
        Comparator<Product> productComparator = Comparator.comparingInt(Product::getCaloriesPer100);
        if (!inAscending) {
            productComparator = productComparator.reversed();
        }
        repository.getAll().sort(productComparator);
    }
}