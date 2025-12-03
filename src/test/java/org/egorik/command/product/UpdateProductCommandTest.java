package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.LeafyVegetable;
import org.egorik.model.Product;
import org.egorik.model.RootVegetable;
import org.egorik.model.Vegetable;
import org.egorik.service.ProductService;
import org.egorik.utils.SelectUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UpdateProductCommandTest {

    private ProductService productService;
    private InputManager inputManager;

    private Command updateProductCommand;

    private ByteArrayOutputStream outputStream;

    private List<Product> products;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);
        updateProductCommand = new UpdateProductCommand(productService, inputManager);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        products = List.of(
                new Product("Tomato", 10, 2, 3, 4),
                new Vegetable("Cucumber", 15, 1, 0, 3, "fresh"),
                new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91),
                new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5)
        );
    }

    @Test
    void shouldReturnWhenProductListIsEmpty() {
        when(productService.isProductsEmpty()).thenReturn(true);

        updateProductCommand.execute();

        assertTrue(outputStream.toString().contains("Product list is empty!"));
        verify(productService, never()).updateProduct(anyInt(), any());
    }

    @Test
    void shouldReturnWhenSelectUtilityReturnsMinusOne() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(-1);

            updateProductCommand.execute();

            verify(productService, never()).updateProduct(anyInt(), any());
        }
    }

    @Test
    void shouldReturnWhenUserDoesNotConfirmUpdate() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(false);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            verify(productService, never()).updateProduct(anyInt(), any());
        }
    }

    @Test
    void shouldUpdateProductName() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(1);
        when(inputManager.getValidString()).thenReturn("New Tomato");

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(0), argThat(p ->
                    "New Tomato".equals(p.name)
            ));
        }
    }

    @Test
    void shouldUpdateProductCalories() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(2);
        when(inputManager.getPositiveInt()).thenReturn(100);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(0), argThat(p ->
                    p.caloriesPer100 == 100
            ));
        }
    }

    @Test
    void shouldUpdateProductProteins() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(3);
        when(inputManager.getPositiveInt()).thenReturn(50);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(0), argThat(p ->
                    p.proteinsPer100 == 50
            ));
        }
    }

    @Test
    void shouldUpdateProductFats() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(4);
        when(inputManager.getPositiveInt()).thenReturn(20);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(0), argThat(p ->
                    p.fatsPer100 == 20
            ));
        }
    }

    @Test
    void shouldUpdateProductCarbs() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(5);
        when(inputManager.getPositiveInt()).thenReturn(30);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(0), argThat(p ->
                    p.carbsPer100 == 30
            ));
        }
    }

    @Test
    void shouldUpdateVegetableType() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 6)).thenReturn(6);
        when(inputManager.getValidString()).thenReturn("organic");

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(1);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(1), argThat(p ->
                    "organic".equals(p.type)
            ));

            assertTrue(outputStream.toString().contains("6 - Vegetable type"));
        }
    }

    @Test
    void shouldUpdateLeafyVegetableWaterPercentage() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 7)).thenReturn(7);
        when(inputManager.getPositiveInt()).thenReturn(95);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(2);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(2), argThat(p ->
                    p.waterPercentage == 95
            ));

            assertTrue(outputStream.toString().contains("7 - Water %"));
        }
    }

    @Test
    void shouldUpdateRootVegetableStarchContent() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 7)).thenReturn(7);
        when(inputManager.getPositiveInt()).thenReturn(15);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(3);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(3), argThat(p ->
                    p.starchContent == 15
            ));

            assertTrue(outputStream.toString().contains("7 - Starch content"));
        }
    }

    @Test
    void shouldUpdateMultipleFieldsInSequence() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, true, true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(1, 2, 5);
        when(inputManager.getValidString()).thenReturn("Updated Name");
        when(inputManager.getPositiveInt()).thenReturn(150, 40);

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            verify(productService).updateProduct(eq(0), argThat(p ->
                    "Updated Name".equals(p.name)
                            && p.caloriesPer100 == 150
                            && p.carbsPer100 == 40
            ));
        }
    }

    @Test
    void shouldDisplayProductToString() {
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(products);
        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(1);
        when(inputManager.getValidString()).thenReturn("Test");

        try (MockedStatic<SelectUtility> mockedStatic = mockStatic(SelectUtility.class)) {
            mockedStatic.when(() -> SelectUtility.selectInd(any(), any())).thenReturn(0);

            updateProductCommand.execute();

            String output = outputStream.toString();
            assertTrue(output.contains("Updated"));
        }
    }
}
