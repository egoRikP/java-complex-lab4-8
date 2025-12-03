package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Ingredient;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.model.Vegetable;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetMoreInfoAboutSaladCommandTest {

    SaladService saladService;
    InputManager inputManager;

    Command getMoreInfoAboutSaladCommand;

    ByteArrayOutputStream outputStream;

    List<Product> products = List.of(
            new Product("Tomato", 10, 2, 3, 4),
            new Vegetable("Onion", 40, 1, 0, 9, "bulb"),
            new Vegetable("Cucumber", 15, 1, 0, 3, "fresh")
    );

    List<Salad> salads = List.of(
            new Salad("GreekSalad", 5, List.of(), List.of(), Set.of()),
            new Salad("VitaminSalad", 2, List.of(new Ingredient(products.get(0), 10)), List.of(), Set.of()),
            new Salad("BoostSalad", 7, List.of(), List.of("first...", "second.."), Set.of()),
            new Salad("other", 7, List.of(new Ingredient(products.get(0), 10)), List.of("first...", "second.."), Set.of("kek"))
    );

    @BeforeEach
    void setUp() {
        saladService = mock(SaladService.class);
        inputManager = mock(InputManager.class);
        getMoreInfoAboutSaladCommand = new GetMoreInfoAboutSaladCommand(saladService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptyProductList() {

        when(saladService.isSaladsEmpty()).thenReturn(true);

        getMoreInfoAboutSaladCommand.execute();

        verify(saladService).isSaladsEmpty();
        assertTrue(outputStream.toString().contains("Salad list is empty!"));

    }

    @Test
    void shouldExit() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> utilities = Mockito.mockStatic(SelectUtility.class)) {
            utilities.when(() -> SelectUtility.selectOne(salads, inputManager))
                    .thenReturn(null);

            getMoreInfoAboutSaladCommand.execute();
        }

        String res = outputStream.toString();

        verify(saladService, times(2)).isSaladsEmpty();
        verify(saladService, times(2)).getAllSalads();

        assertTrue(res.contains("leaving"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void shouldPrintDetailedInfoForDifferentSalads(int index) {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> utilities = Mockito.mockStatic(SelectUtility.class)) {
            utilities.when(() -> SelectUtility.selectOne(salads, inputManager))
                    .thenReturn(salads.get(index));

            getMoreInfoAboutSaladCommand.execute();
        }

        String res = outputStream.toString();

        verify(saladService, times(2)).isSaladsEmpty();
        verify(saladService, times(2)).getAllSalads();

        assertTrue(res.contains("All salads"));
        assertTrue(res.contains(salads.get(index).detailedInfo()));
    }

}