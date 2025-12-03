package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.LeafyVegetable;
import org.egorik.model.Product;
import org.egorik.model.RootVegetable;
import org.egorik.model.Vegetable;
import org.egorik.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetProductsByNameCommandTest {

    ProductService productService;
    InputManager inputManager;

    Command getProductsByName;

    ByteArrayOutputStream outputStream;

    List<Product> products = List.of(
            new Product("Tomato", 10, 2, 3, 4),
            new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
            new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91),
            new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5)
    );

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);
        getProductsByName = new GetProductsByNameCommand(productService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptyProductList() {
        when(productService.isProductsEmpty()).thenReturn(true);

        getProductsByName.execute();

        verify(productService).isProductsEmpty();
        assertTrue(outputStream.toString().contains("Product list is empty!"));
    }

    @Test
    void shouldFindProductsByName() {
        when(inputManager.getValidString()).thenReturn("o");
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getProductsByName("o")).thenReturn(List.of(products.get(0), products.get(1), products.get(3)));

        getProductsByName.execute();
        String res = outputStream.toString();

        verify(inputManager).getValidString();
        verify(productService).isProductsEmpty();
        verify(productService).getProductsByName("o");
        assertTrue(res.contains("Products with name"));
        assertTrue(res.contains(products.get(0).toString()));
        assertTrue(res.contains(products.get(1).toString()));
        assertTrue(res.contains(products.get(3).toString()));
    }

    @Test
    void shouldNotFindProductsByName() {
        when(inputManager.getValidString()).thenReturn("other product");
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getProductsByName("other product")).thenReturn(List.of());

        getProductsByName.execute();
        String res = outputStream.toString();

        verify(inputManager).getValidString();
        verify(productService).isProductsEmpty();
        verify(productService).getProductsByName("other product");
        assertFalse(res.contains(products.get(0).toString()));
        assertFalse(res.contains(products.get(1).toString()));
        assertFalse(res.contains(products.get(2).toString()));
        assertFalse(res.contains(products.get(3).toString()));
    }
}