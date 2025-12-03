package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.*;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DeleteProductCommandTest {

    ProductService productService;
    SaladService saladService;

    InputManager inputManager;

    Command deleteProductCommand;

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
        saladService = mock(SaladService.class);
        inputManager = mock(InputManager.class);
        deleteProductCommand = new DeleteProductCommand(productService, saladService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptyProductList() {

        when(productService.isProductsEmpty()).thenReturn(true);

        deleteProductCommand.execute();

        verify(productService).isProductsEmpty();
        assertTrue(outputStream.toString().contains("is empty"));

    }

    @Test
    void shouldExit() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.getValidIntInRange(-1, 3)).thenReturn(-1);

        deleteProductCommand.execute();

        verify(productService).isProductsEmpty();
        verify(productService, never()).deleteProduct(0);
    }


    @Test
    void shouldDeleteProduct() {

        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.getValidIntInRange(-1, 3)).thenReturn(0);
        when(saladService.getSaladsByProducts(any(Product.class))).thenReturn(List.of());
        when(inputManager.isContinue(anyString())).thenReturn(true);

        deleteProductCommand.execute();

        verify(productService).isProductsEmpty();
        verify(productService, atLeastOnce()).getAllProducts();
        verify(productService).deleteProduct(0);
        verify(inputManager).isContinue(contains(String.format("Delete %s ?", products.get(0))));
    }

    @Test
    void shouldNotContinueDeleteProduct() {

        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.getValidIntInRange(-1, 3)).thenReturn(0);
        when(saladService.getSaladsByProducts(any(Product.class))).thenReturn(List.of());
        when(inputManager.isContinue(anyString())).thenReturn(false);

        deleteProductCommand.execute();

        verify(productService).isProductsEmpty();
        verify(productService, atLeastOnce()).getAllProducts();
        verify(productService, never()).deleteProduct(0);
        verify(inputManager).isContinue(contains(String.format("Delete %s ?", products.get(0))));
    }

    @Test
    void shouldNotDeleteProductBecauseOfSalads() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.getValidIntInRange(-1, 3)).thenReturn(0);
        when(saladService.getSaladsByProducts(any(Product.class))).thenReturn(
                List.of(new Salad("test", 10, new ArrayList<>(), new ArrayList<>(), new HashSet<>()))
        );
        when(inputManager.isContinue(anyString())).thenReturn(true);

        deleteProductCommand.execute();

        String res = outputStream.toString();
        verify(productService).isProductsEmpty();
        verify(saladService).getSaladsByProducts(products.get(0));
        verify(productService, times(0)).deleteProduct(0);
        assertTrue(res.contains("Can't delete product"));
        assertTrue(res.contains(products.get(0).toString()));
    }

}