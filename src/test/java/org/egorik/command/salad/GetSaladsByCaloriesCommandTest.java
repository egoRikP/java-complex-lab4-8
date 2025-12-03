package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.*;
import org.egorik.service.SaladService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetSaladsByCaloriesCommandTest {

    SaladService saladService;
    InputManager inputManager;

    Command getSaladsByCalories;

    ByteArrayOutputStream outputStream;

    List<Product> products = List.of(
            new Product("Tomato", 10, 2, 3, 4),
            new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
            new Vegetable("Cucumber", 15, 1, 0, 3, "fresh"),
            new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91),
            new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5),
            new Product("Avocado", 160, 2, 15, 9),
            new Product("Apple", 54, 34, 5, 9)
    );

    List<Salad> salads = List.of(
            new Salad(
                    "GreekSalad",
                    5,
                    List.of(
                            new Ingredient(products.get(0), 100.0),
                            new Ingredient(products.get(1), 50.0)
                    ),
                    List.of("Cut vegetables", "Mix gently"),
                    Set.of("fresh", "simple")
            ),
            new Salad(
                    "VitaminSalad",
                    2,
                    List.of(
                            new Ingredient(products.get(3), 70.0),
                            new Ingredient(products.get(4), 80.0)
                    ),
                    List.of("Shred carrot", "Add spinach"),
                    Set.of("vitamins")
            ),
            new Salad(
                    "BoostSalad",
                    7,
                    List.of(
                            new Ingredient(products.get(2), 25.0),
                            new Ingredient(products.get(0), 60.0)
                    ),
                    List.of("Mix ingredients"),
                    Set.of("light")
            )
    );


    @BeforeEach
    void setUp() {
        saladService = mock(SaladService.class);
        inputManager = mock(InputManager.class);
        getSaladsByCalories = new GetSaladsByCaloriesCommand(saladService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }


    @Test
    void shouldGiveEmptyProductList() {

        when(saladService.isSaladsEmpty()).thenReturn(true);

        getSaladsByCalories.execute();

        verify(saladService).isSaladsEmpty();
        assertTrue(outputStream.toString().contains("Salad list is empty!"));

    }

    @Test
    void shouldGetProductsByCalories() {

        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(inputManager.getPositiveInt()).thenReturn(0, 10);
        when(saladService.getSaladsByTotalCalories(0, 10))
                .thenReturn(List.of(salads.get(0)));

        getSaladsByCalories.execute();
        String res = outputStream.toString();

        verify(saladService).isSaladsEmpty();
        verify(inputManager, times(2)).getPositiveInt();
        verify(saladService).getSaladsByTotalCalories(0, 10);
        assertTrue(res.contains("Salads in calories range"));
        assertTrue(res.contains(salads.get(0).toString()));
    }


    @Test
    void shouldNotGetProductsByCalories() {

        when(saladService.isSaladsEmpty()).thenReturn(false);

        when(inputManager.getPositiveInt()).thenReturn(10000, 100000);

        when(saladService.getSaladsByTotalCalories(10000, 100000))
                .thenReturn(List.of());

        getSaladsByCalories.execute();
        String res = outputStream.toString();

        verify(saladService).isSaladsEmpty();
        verify(inputManager, times(2)).getPositiveInt();
        verify(saladService).getSaladsByTotalCalories(10000, 100000);
        assertTrue(res.contains("Can't find any salads"));
    }


}