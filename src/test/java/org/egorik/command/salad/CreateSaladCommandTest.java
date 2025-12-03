package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Product;
import org.egorik.service.ProductService;
import org.egorik.service.SaladService;
import org.egorik.utils.SelectUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CreateSaladCommandTest {

    SaladService saladService;
    ProductService productService;
    InputManager inputManager;

    Command createSaladCommand;
    ByteArrayOutputStream out;

    Product tomato = new Product("Tomato", 10, 1, 2, 3);
    Product cucumber = new Product("Cucumber", 15, 1, 0, 3);

    @BeforeEach
    void setup() {
        saladService = mock(SaladService.class);
        productService = mock(ProductService.class);
        inputManager = mock(InputManager.class);

        createSaladCommand = new CreateSaladCommand(saladService, productService, inputManager);

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @Test
    void shouldStopIfNoProductsAndUserDeclines() {

        when(productService.isProductsEmpty()).thenReturn(true);
        when(inputManager.isContinue(anyString())).thenReturn(false);

        createSaladCommand.execute();

        verify(saladService, never()).addSalad(any());
        assertTrue(out.toString().contains("No products"));
    }

    @Test
    void shouldAddSingleIngredient() {

        when(productService.isProductsEmpty()).thenReturn(false);

        when(inputManager.getValidString()).thenReturn(
                "Veggie",
                "tomato",
                "q"
        );

        when(productService.getProductsByName("tomato"))
                .thenReturn(List.of(tomato));

        when(inputManager.getPositiveInt())
                .thenReturn(100)
                .thenReturn(5);

        when(inputManager.getString()).thenReturn("newyear");
        when(inputManager.isContinue("Add other ingredient?")).thenReturn(false);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {
            util.when(() -> SelectUtility.selectInd(List.of(tomato), inputManager))
                    .thenReturn(0);
        }

        createSaladCommand.execute();

        verify(saladService).addSalad(argThat(s ->
                s.getIngredients().size() == 1 &&
                        s.getIngredients().get(0).getProduct().equals(tomato)
        ));
    }


    @Test
    void shouldCreateInstructions() {

        when(productService.isProductsEmpty()).thenReturn(false);

        when(inputManager.getValidString()).thenReturn(
                "Salad1",
                "tomato",
                "Cut",
                "Mix",
                "q"
        );

        when(productService.getProductsByName("tomato"))
                .thenReturn(List.of(tomato));

        when(inputManager.getPositiveInt()).thenReturn(100, 10);
        when(inputManager.getString()).thenReturn("party");
        when(inputManager.isContinue("Add other ingredient?")).thenReturn(false);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {
            util.when(() -> SelectUtility.selectInd(List.of(tomato), inputManager))
                    .thenReturn(0);
        }

        createSaladCommand.execute();

        verify(saladService).addSalad(argThat(s ->
                s.getInstruction().size() == 2 &&
                        s.getInstruction().contains("Cut") &&
                        s.getInstruction().contains("Mix")
        ));
    }

    @Test
    void shouldSkipTagsWhenBlankAndUserSkips() {

        when(productService.isProductsEmpty()).thenReturn(false);

        when(inputManager.getValidString()).thenReturn(
                "Salad",
                "tomato",
                "q"
        );

        when(productService.getProductsByName("tomato"))
                .thenReturn(List.of(tomato));

        when(inputManager.getString())
                .thenReturn("     ")
                .thenReturn("       ");

        when(inputManager.isContinue("Skip tags?")).thenReturn(true);

        when(inputManager.isContinue("Add other ingredient?")).thenReturn(false);

        when(inputManager.getPositiveInt()).thenReturn(100, 5);

        try (MockedStatic<SelectUtility> util = Mockito.mockStatic(SelectUtility.class)) {
            util.when(() -> SelectUtility.selectInd(List.of(tomato), inputManager))
                    .thenReturn(0);
        }

        createSaladCommand.execute();

        verify(saladService).addSalad(argThat(s ->
                s.getTags().size() == 1 && s.getTags().contains("")
        ));
    }
}
