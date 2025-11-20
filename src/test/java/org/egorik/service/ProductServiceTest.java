package org.egorik.service;

import org.egorik.model.*;
import org.egorik.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(new MemoryRepository<>());

        productService.addProduct(new Product("Tomato", 10, 2, 3, 4));
        productService.addProduct(new Vegetable("Onion", 40, 1, 0, 9, "bulb"));
        productService.addProduct(new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91));
        productService.addProduct(new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5));
    }

    @Test
    void shouldAddProduct() {
        int sizeBeforeAdd = productService.getAllProducts().size();

        productService.addProduct(new Product("Potato", 20, 2, 3, 4));

        assertEquals(sizeBeforeAdd + 1, productService.getAllProducts().size());
        assertTrue(productService.isProductExists(new Product("Potato", 20, 2, 3, 4)));
    }

    @Test
    void shouldNotAddDuplicateProduct() {
        int sizeBeforeAdd = productService.getAllProducts().size();
        productService.addProduct(new Product("Tomato", 10, 2, 3, 4));
        assertEquals(sizeBeforeAdd, productService.getAllProducts().size());
    }

    @Test
    void shouldGetAllProducts() {
        assertEquals(4, productService.getAllProducts().size());
        assertTrue(productService.isProductExists(new Product("Tomato", 10, 2, 3, 4)));
        assertTrue(productService.isProductExists(new Vegetable("Onion", 40, 1, 0, 9, "bulb")));
        assertTrue(productService.isProductExists(new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91)));
        assertTrue(productService.isProductExists(new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5)));
    }

    @Test
    void shouldGetProductsByCalories() {
        assertEquals(1, productService.getProductsByCalories(10, 10).size());
        assertEquals(1, productService.getProductsByCalories(40, 40).size());
        assertEquals(1, productService.getProductsByCalories(23, 23).size());
        assertEquals(1, productService.getProductsByCalories(41 + 5 * 4, 41 + 5 * 4).size()); //calories = base calories + 4* starch

        assertEquals(4, productService.getProductsByCalories(0, 41 + 5 * 4).size());
        assertEquals(3, productService.getProductsByCalories(23, 41 + 5 * 4).size());
        assertEquals(2, productService.getProductsByCalories(10, 23).size());
    }

    @Test
    void shouldNotGetProductsByCalories() {
        assertEquals(0, productService.getProductsByCalories(1, 5).size());
        assertEquals(0, productService.getProductsByCalories(0, 9).size());
        assertEquals(0, productService.getProductsByCalories(150, 200).size());
    }

    @Test
    void shouldGetProductsByName() {
        assertEquals(1, productService.getProductsByName("Tomato").size());
        assertEquals(List.of(new Product("Tomato", 10, 2, 3, 4)), productService.getProductsByName("Tomato"));

        assertEquals(3, productService.getProductsByName("o").size());
        assertEquals(List.of(
                new Product("Tomato", 10, 2, 3, 4),
                new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
                new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5)
        ), productService.getProductsByName("o"));
    }

    @Test
    void shouldGetProductsByFormatedName() {
        assertEquals(1, productService.getProductsByName(" TOMAto ").size());
        assertEquals(List.of(new Product("Tomato", 10, 2, 3, 4)), productService.getProductsByName("TOMAto"));

        assertEquals(3, productService.getProductsByName("   O    ").size());
        assertEquals(List.of(
                new Product("Tomato", 10, 2, 3, 4),
                new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
                new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5)
        ), productService.getProductsByName("    O  "));
    }

    @Test
    void shouldSortProductsByCaloriesInAsc() {

        List<Product> sortedProducts = List.of(new Product("Tomato", 10, 2, 3, 4),
                new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91),
                new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
                new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5));

        productService.sortProductsByCalories(true);

        assertEquals(10, productService.getAllProducts().getFirst().getCaloriesPer100());
        assertEquals(41 + 5 * 4, productService.getAllProducts().getLast().getCaloriesPer100());

        assertEquals(sortedProducts, productService.getAllProducts());
    }

    @Test
    void shouldSortProductsByCaloriesInDesc() {

        List<Product> sortedProducts = List.of(
                new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5),
                new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
                new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91),
                new Product("Tomato", 10, 2, 3, 4)
        );

        productService.sortProductsByCalories(false);

        assertEquals(41 + 5 * 4, productService.getAllProducts().getFirst().getCaloriesPer100());
        assertEquals(10, productService.getAllProducts().getLast().getCaloriesPer100());

        assertEquals(sortedProducts, productService.getAllProducts());
    }

    @Test
    void shouldDeleteProduct() {
        int sizeBeforeDelete = productService.getAllProducts().size();

        productService.deleteProduct(0);
        List<Product> afterDelete = List.of(
                new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
                new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91),
                new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5));

        assertEquals(sizeBeforeDelete - 1, productService.getAllProducts().size());
        assertFalse(productService.isProductExists(new Product("Tomato", 10, 2, 3, 4)));
        assertEquals(afterDelete, productService.getAllProducts());
    }

    @Test
    void shouldNotDeleteProduct() {
        int sizeBeforeDelete = productService.getAllProducts().size();

        assertThrows(IndexOutOfBoundsException.class, () -> productService.deleteProduct(23));
        List<Product> afterDelete = List.of(
                new Product("Tomato", 10, 2, 3, 4),
                new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
                new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91),
                new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5));

        assertEquals(sizeBeforeDelete, productService.getAllProducts().size());
        assertEquals(afterDelete, productService.getAllProducts());
    }

    @Test
    void shouldBeEmpty() {

        int size = productService.getAllProducts().size();
        for (int i = 0; i < size; i++) {
            productService.deleteProduct(0);
        }

        assertTrue(productService.isProductsEmpty());
    }

    @Test
    void shouldNotBeEmpty() {
        assertFalse(productService.isProductsEmpty());
    }

    @Test
    void shouldUpdateProduct() {
        List<Product> beforeUpdate = productService.getAllProducts().stream()
                .map(p -> {
                    if (p instanceof RootVegetable rv) return new RootVegetable(rv);
                    if (p instanceof LeafyVegetable lv) return new LeafyVegetable(lv);
                    if (p instanceof Vegetable v) return new Vegetable(v);
                    return new Product(p);
                }).toList();

        ProductPatch productPatch = new ProductPatch();
        productPatch.name = "newName";
        productPatch.carbsPer100 = 12;
        productPatch.fatsPer100 = 54;

        productService.updateProduct(0, productPatch);

        assertEquals(productPatch.name, productService.getAllProducts().getFirst().getName());
        assertEquals(productPatch.carbsPer100, productService.getAllProducts().getFirst().getCarbsPer100());
        assertEquals(productPatch.fatsPer100, productService.getAllProducts().getFirst().getFatsPer100());

        assertNotEquals(beforeUpdate, productService.getAllProducts());
    }

    @Test
    void shouldNotUpdateProduct() {
        List<Product> beforeUpdate = productService.getAllProducts().stream()
                .map(p -> {
                    if (p instanceof RootVegetable rv) return new RootVegetable(rv);
                    if (p instanceof LeafyVegetable lv) return new LeafyVegetable(lv);
                    if (p instanceof Vegetable v) return new Vegetable(v);
                    return new Product(p);
                })
                .toList();

        ProductPatch productPatch = new ProductPatch();
        productPatch.name = "newName";
        productPatch.carbsPer100 = 12;
        productPatch.fatsPer100 = 54;

        assertThrows(IndexOutOfBoundsException.class, () -> productService.updateProduct(540, productPatch));

        assertEquals(beforeUpdate, productService.getAllProducts());
    }
}