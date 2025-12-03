package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.manager.InputManager;
import org.egorik.model.Salad;
import org.egorik.service.SaladService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

class DeleteSaladCommandTest {

    SaladService saladService;

    InputManager inputManager;

    Command deleteProductCommand;

    ByteArrayOutputStream outputStream;

    List<Salad> salads = List.of(
            new Salad("GreekSalad", 5, List.of(), List.of(), Set.of()),
            new Salad("VitaminSalad", 2, List.of(), List.of(), Set.of()),
            new Salad("BoostSalad", 7, List.of(), List.of(), Set.of())
    );

    @BeforeEach
    void setUp() {
        saladService = mock(SaladService.class);
        inputManager = mock(InputManager.class);
        deleteProductCommand = new DeleteSaladCommand(saladService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptyProductList() {

        when(saladService.isSaladsEmpty()).thenReturn(true);

        deleteProductCommand.execute();

        verify(saladService).isSaladsEmpty();
        assertTrue(outputStream.toString().contains("is empty"));

    }

    @Test
    void shouldExit() {
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);
        when(inputManager.getValidIntInRange(-1, 2)).thenReturn(-1);

        deleteProductCommand.execute();

        verify(saladService).isSaladsEmpty();
        verify(saladService, never()).deleteSalad(0);
    }


    @Test
    void shouldDeleteProduct() {

        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);
        when(inputManager.getValidIntInRange(-1, 2)).thenReturn(0);
        when(inputManager.isContinue(anyString())).thenReturn(true);

        deleteProductCommand.execute();

        verify(saladService).isSaladsEmpty();
        verify(saladService, atLeastOnce()).getAllSalads();
        verify(saladService).deleteSalad(0);
        verify(inputManager).isContinue(contains(String.format("Delete %s ?", salads.get(0))));
    }

    @Test
    void shouldNotDeleteProduct() {

        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(salads);
        when(inputManager.getValidIntInRange(-1, 2)).thenReturn(0);
        when(inputManager.isContinue(anyString())).thenReturn(false);

        deleteProductCommand.execute();

        verify(saladService).isSaladsEmpty();
        verify(saladService, atLeastOnce()).getAllSalads();
        verify(saladService, never()).deleteSalad(0);
        verify(inputManager).isContinue(contains(String.format("Delete %s ?", salads.get(0))));
    }

}