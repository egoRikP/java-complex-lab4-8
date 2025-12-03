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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetProductsByCaloriesCommandTest {

    ProductService productService;
    InputManager inputManager;

    Command getProductsByCalories;

    ByteArrayOutputStream outputStream;

    List<Product> products = List.of(
            new Product("Tomato", 10, 2, 3, 4),
            new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
            new LeafyVegetable("Spinach", 5, 3, 0, 4, "leaf", 91),
            new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5)
    );

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);
        getProductsByCalories = new GetProductsByCaloriesCommand(productService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }


    @Test
    void shouldGiveEmptyProductList() {

        when(productService.isProductsEmpty()).thenReturn(true);

        getProductsByCalories.execute();

        verify(productService).isProductsEmpty();
        assertTrue(outputStream.toString().contains("Product list is empty!"));

    }

    @Test
    void shouldGetProductsByCalories() {

        when(inputManager.getPositiveInt()).thenReturn(0);
        when(inputManager.getValidInt()).thenReturn(10);
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getProductsByCalories(0, 10)).thenReturn(List.of(products.get(0)));

        getProductsByCalories.execute();
        String res = outputStream.toString();

        verify(inputManager).getPositiveInt();
        verify(inputManager).getValidInt();
        verify(productService).isProductsEmpty();
        verify(productService).getProductsByCalories(0, 10);
        assertTrue(res.contains("Products with calories in range"));
        assertTrue(res.contains(products.get(0).toString()));

    }

    @Test
    void shouldNotGetProductsByCalories() {
        when(inputManager.getPositiveInt()).thenReturn(10000);
        when(inputManager.getValidInt()).thenReturn(100000);
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getProductsByCalories(10000, 100000)).thenReturn(List.of());

        getProductsByCalories.execute();
        String res = outputStream.toString();

        verify(productService).isProductsEmpty();
        verify(inputManager).getPositiveInt();
        verify(inputManager).getValidInt();
        verify(productService).getProductsByCalories(10000, 100000);
        assertTrue(res.contains("is empty"));
    }

    @Test
    void shouldGetInputCaloriesWarning() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(inputManager.getPositiveInt()).thenReturn(0);
        when(inputManager.getValidInt()).thenReturn(-10, 10);

        getProductsByCalories.execute();
        
        verify(productService).isProductsEmpty();
        verify(inputManager, times(1)).getPositiveInt();
        verify(inputManager, times(2)).getValidInt();
        assertTrue(outputStream.toString().contains("Bigger or equals"));

    }

}