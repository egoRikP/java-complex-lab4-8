package org.egorik.command.product;

import org.egorik.manager.InputManager;
import org.egorik.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SortProductByCaloriesCommandTest {

    ProductService productService;
    InputManager inputManager;

    ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldNotSortProductsBecauseOfEmptyList() {
        when(productService.isProductsEmpty()).thenReturn(true);

        new SortProductByCaloriesCommand(productService, inputManager).execute();

        assertTrue(outputStream.toString().contains("is empty"));
    }

    @Test
    void shouldExit() {
        when(inputManager.isContinue(anyString())).thenReturn(false);

        new SortProductByCaloriesCommand(productService, inputManager).execute();

        verify(productService, never()).sortProductsByCalories(anyBoolean());
    }

    @Test
    void shouldSortInAscProducts() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(inputManager.isContinue(anyString())).thenReturn(true, true);

        new SortProductByCaloriesCommand(productService, inputManager).execute();

        verify(productService,atLeastOnce()).isProductsEmpty();
        verify(productService).sortProductsByCalories(true);
    }

    @Test
    void shouldSortInDescProducts() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);

        new SortProductByCaloriesCommand(productService, inputManager).execute();

        verify(productService,atLeastOnce()).isProductsEmpty();
        verify(productService).sortProductsByCalories(false);
    }

}