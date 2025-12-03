package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Ingredient;
import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UpdateSaladCommandTest {

    SaladService saladService;
    ProductService productService;
    InputManager inputManager;

    Command updateSaladCommand;
    ByteArrayOutputStream output;

    Salad salad;
    List<Salad> salads;

    @BeforeEach
    void setup() {
        saladService = mock(SaladService.class);
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);

        salad = new Salad(
                "Greek",
                10,
                List.of(new Ingredient(new Product("Tomato", 10, 1, 2, 3), 100)),
                List.of("cut", "mix"),
                new HashSet<>(Set.of("easy"))
        );

        salads = List.of(salad);

        updateSaladCommand = new UpdateSaladCommand(saladService, productService, inputManager);

        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }


    @Test
    void shouldStopWhenSaladsEmpty() {
        when(saladService.isSaladsEmpty()).thenReturn(true);

        updateSaladCommand.execute();

        assertTrue(output.toString().contains("Salad list is empty"));
        verify(saladService, never()).updateSalad(anyInt(), any());
    }


    @Test
    void shouldStopOnIndMinus1() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {
            util.when(() -> SelectUtility.selectInd(salads, inputManager)).thenReturn(-1);

            updateSaladCommand.execute();
        }

        verify(saladService, never()).updateSalad(anyInt(), any());
    }


    @Test
    void shouldStopWhenContinueIsFalse() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);
        when(inputManager.isContinue("Want continue to update?")).thenReturn(false);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {
            util.when(() -> SelectUtility.selectInd(salads, inputManager)).thenReturn(0);
        }

        updateSaladCommand.execute();

        verify(saladService, never()).updateSalad(anyInt(), any());
    }


    @Test
    void shouldUpdateName() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {
            util.when(() -> SelectUtility.selectInd(salads, inputManager)).thenReturn(0);
        }

        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(1);
        when(inputManager.getValidString()).thenReturn("NewName");

        updateSaladCommand.execute();

        verify(saladService).updateSalad(eq(0), argThat(p ->
                "NewName".equals(p.name)
        ));
    }


    @Test
    void shouldAddIngredient() {
        Product p = new Product("Onion", 40, 1, 0, 9);

        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(List.of(p));

        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {

            util.when(() -> SelectUtility.selectInd(salads, inputManager))
                    .thenReturn(0);

            util.when(() -> SelectUtility.selectOne(anyList(), eq(inputManager)))
                    .thenReturn(p);


            when(inputManager.getValidIntInRange(anyInt(), anyInt()))
                    .thenReturn(2, 1);

            when(inputManager.isContinue(anyString()))
                    .thenReturn(true, false);

            when(inputManager.getPositiveInt()).thenReturn(50);

            updateSaladCommand.execute();
        }

        verify(saladService).updateSalad(eq(0), argThat(patch ->
                patch.ingredients.size() == 2 &&
                        patch.ingredients.get(1).getProduct().equals(p) &&
                        patch.ingredients.get(1).getWeight() == 50
        ));
    }


    @Test
    void shouldAddInstruction() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {

            util.when(() -> SelectUtility.selectInd(salads, inputManager))
                    .thenReturn(0);

            when(inputManager.getValidIntInRange(anyInt(), anyInt()))
                    .thenReturn(3, 1);

            when(inputManager.isContinue(anyString()))
                    .thenReturn(true, false);

            when(inputManager.getValidString())
                    .thenReturn("Slice veggies");

            updateSaladCommand.execute();
        }

        verify(saladService).updateSalad(eq(0), argThat(patch ->
                patch.instruction.contains("Slice veggies")
        ));
    }


    @Test
    void shouldAddTag() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {

            util.when(() -> SelectUtility.selectInd(salads, inputManager))
                    .thenReturn(0);

            when(inputManager.getValidIntInRange(anyInt(), anyInt()))
                    .thenReturn(4, 1);

            when(inputManager.isContinue(anyString()))
                    .thenReturn(true, false);

            when(inputManager.getValidString())
                    .thenReturn("fresh");

            updateSaladCommand.execute();
        }

        verify(saladService).updateSalad(eq(0), argThat(patch ->
                patch.tags.contains("fresh")
        ));
    }


    @Test
    void shouldUpdateTime() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {
            util.when(() -> SelectUtility.selectInd(salads, inputManager)).thenReturn(0);
        }

        when(inputManager.isContinue(anyString())).thenReturn(true, false);
        when(inputManager.getValidIntInRange(1, 5)).thenReturn(5);
        when(inputManager.getPositiveInt()).thenReturn(99);

        updateSaladCommand.execute();

        verify(saladService).updateSalad(eq(0), argThat(patch ->
                patch.timeToFinish == 99
        ));
    }
}
