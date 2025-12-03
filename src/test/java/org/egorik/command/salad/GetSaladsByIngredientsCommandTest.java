package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Ingredient;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.model.Vegetable;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetSaladsByIngredientsCommandTest {

    SaladService saladService;
    ProductService productService;
    InputManager inputManager;

    Command getSaladsByIngredientsCommand;

    ByteArrayOutputStream out;

    List<Product> products = List.of(
            new Product("Tomato", 10, 2, 3, 4),
            new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
            new Vegetable("Cucumber", 15, 1, 0, 3, "fresh")
    );

    List<Salad> salads = List.of(
            new Salad("GreekSalad", 5, List.of(
                    new Ingredient(products.get(0), 100.0),
                    new Ingredient(products.get(1), 50.0)),
                    List.of(), Set.of()),
            new Salad("BoostSalad", 7, List.of(
                    new Ingredient(products.get(2), 25.0),
                    new Ingredient(products.get(0), 60.0)
            ), List.of(), Set.of())
    );

    @BeforeEach
    void setUp() {
        saladService = mock(SaladService.class);
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);

        getSaladsByIngredientsCommand = new GetSaladsByIngredientsCommand(saladService, productService, inputManager);

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @Test
    void shouldPrintEmptySaladList() {

        when(saladService.isSaladsEmpty()).thenReturn(true);

        getSaladsByIngredientsCommand.execute();

        verify(saladService).isSaladsEmpty();
        assertTrue(out.toString().contains("Salad list is empty!"));
    }

    @Test
    void shouldPrintEmptyProductList() {

        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(productService.isProductsEmpty()).thenReturn(true);

        getSaladsByIngredientsCommand.execute();

        verify(productService).isProductsEmpty();
        assertTrue(out.toString().contains("Product list is empty!"));
    }

    @Test
    void shouldFindSaladsByIngredients() {

        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(productService.isProductsEmpty()).thenReturn(false);

        when(productService.getAllProducts()).thenReturn(products);
        when(saladService.getSaladsByProducts(List.of(products.get(0))))
                .thenReturn(List.of(salads.get(0), salads.get(1)));

        try (MockedStatic<SelectUtility> mocked = mockStatic(SelectUtility.class)) {

            mocked.when(() -> SelectUtility.selectMany(products, inputManager))
                    .thenReturn(List.of(products.get(0)));

            getSaladsByIngredientsCommand.execute();

            verify(saladService).getSaladsByProducts(List.of(products.get(0)));

            String result = out.toString();

            assertTrue(result.contains("Salads with products"));
            assertTrue(result.contains("GreekSalad"));
            assertTrue(result.contains("BoostSalad"));
        }
    }
}
