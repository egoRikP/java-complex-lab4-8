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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CreateProductCommandTest {

    ProductService productService;
    InputManager inputManager;

    Command createProductCommand;

    ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);
        createProductCommand = new CreateProductCommand(productService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldCreateNormalProduct() {
        Product product = new Product("NormalProduct", 1, 2, 3, 4);
        when(inputManager.getValidIntInRange(1, 4)).thenReturn(1);
        when(inputManager.getValidString()).thenReturn(product.getName());
        when(inputManager.getPositiveInt())
                .thenReturn(product.getCaloriesPer100())
                .thenReturn(product.getProteinsPer100())
                .thenReturn(product.getFatsPer100())
                .thenReturn(product.getCarbsPer100());

        createProductCommand.execute();
        String res = outputStream.toString();

        verify(inputManager, times(4)).getPositiveInt();
        verify(productService).addProduct(product);
        assertTrue(res.contains("Added new product"));
    }

    @Test
    void shouldNotCreateNormalProductBecauseOfDuplicate() {

        Product product = new Product("NormalProduct", 1, 2, 3, 4);
        when(productService.isProductExists(product)).thenReturn(true);
        when(inputManager.getValidIntInRange(1, 4)).thenReturn(1);
        when(inputManager.getValidString()).thenReturn(product.getName());
        when(inputManager.getPositiveInt())
                .thenReturn(product.getCaloriesPer100())
                .thenReturn(product.getProteinsPer100())
                .thenReturn(product.getFatsPer100())
                .thenReturn(product.getCarbsPer100());

        createProductCommand.execute();
        String res = outputStream.toString();

        verify(inputManager, times(4)).getPositiveInt();
        verify(inputManager).getValidString();
        verify(productService).isProductExists(product);
        verify(productService, never()).addProduct(product);
        assertTrue(res.contains("exists"));
    }

    @Test
    void shouldCreateVegetableProduct() {
        Vegetable product = new Vegetable("VegetableProduct", 1, 2, 3, 4, "type");
        when(inputManager.getValidIntInRange(1, 4)).thenReturn(2);
        when(inputManager.getValidString()).thenReturn(product.getName(), product.getType());
        when(inputManager.getPositiveInt())
                .thenReturn(product.getCaloriesPer100())
                .thenReturn(product.getProteinsPer100())
                .thenReturn(product.getFatsPer100())
                .thenReturn(product.getCarbsPer100());

        createProductCommand.execute();
        String res = outputStream.toString();

        verify(inputManager, times(4)).getPositiveInt();
        verify(inputManager, times(2)).getValidString();
        verify(productService, times(1))
                .addProduct(product);
        assertTrue(res.contains("Added new product"));

    }

    @Test
    void shouldCreateLeafyProduct() {
        LeafyVegetable product = new LeafyVegetable("LeafyProduct", 1, 2, 3, 4, "type", 10);
        when(inputManager.getValidIntInRange(1, 4)).thenReturn(3);
        when(inputManager.getValidString()).thenReturn(product.getName(), product.getType());
        when(inputManager.getPositiveInt())
                .thenReturn(product.getCaloriesPer100())
                .thenReturn(product.getProteinsPer100())
                .thenReturn(product.getFatsPer100())
                .thenReturn(product.getCarbsPer100())
                .thenReturn(product.getWaterPercentage());

        createProductCommand.execute();
        String res = outputStream.toString();

        verify(inputManager, times(5)).getPositiveInt();
        verify(inputManager, times(2)).getValidString();
        verify(productService).addProduct(product);
        assertTrue(res.contains("Added new product"));
    }

    @Test
    void shouldCreateRootProduct() {
        RootVegetable normalProduct = new RootVegetable("RootProduct", 1, 2, 3, 4, "type", 10);
        when(inputManager.getValidIntInRange(1, 4)).thenReturn(4);
        when(inputManager.getValidString()).thenReturn(normalProduct.getName(), normalProduct.getType());
        when(inputManager.getPositiveInt())
                .thenReturn(normalProduct.getCaloriesPer100())
                .thenReturn(normalProduct.getProteinsPer100())
                .thenReturn(normalProduct.getFatsPer100())
                .thenReturn(normalProduct.getCarbsPer100())
                .thenReturn(normalProduct.getStarchContent());

        createProductCommand.execute();
        String res = outputStream.toString();

        verify(inputManager, times(5)).getPositiveInt();
        verify(inputManager, times(2)).getValidString();
        verify(productService, times(1))
                .addProduct(argThat(
                        product ->
                                product instanceof RootVegetable rootVegetable &&
                                        rootVegetable.getBaseCalories() == normalProduct.getCaloriesPer100() &&
                                        rootVegetable.getProteinsPer100() == normalProduct.getProteinsPer100() &&
                                        rootVegetable.getCarbsPer100() == normalProduct.getCarbsPer100() &&
                                        rootVegetable.getFatsPer100() == normalProduct.getFatsPer100() &&
                                        rootVegetable.getName().equals(normalProduct.getName()) &&
                                        rootVegetable.getType().equals(normalProduct.getType())

                ));
        assertTrue(res.contains("Added new product"));
    }

}