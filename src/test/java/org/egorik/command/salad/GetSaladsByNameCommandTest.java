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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetSaladsByNameCommandTest {

    SaladService saladService;
    InputManager inputManager;

    Command getSaladsByNameCommand;

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
        getSaladsByNameCommand = new GetSaladsByNameCommand(saladService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptySaladList() {
        when(saladService.isSaladsEmpty()).thenReturn(true);

        getSaladsByNameCommand.execute();

        verify(saladService).isSaladsEmpty();
        assertTrue(outputStream.toString().contains("Salad list is empty!"));
    }

    @Test
    void shouldFindSaladsByName() {
        when(inputManager.getValidString()).thenReturn("t");
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getSaladsByName("t")).thenReturn(List.of(salads.get(1), salads.get(2)));

        getSaladsByNameCommand.execute();
        String res = outputStream.toString();

        verify(inputManager).getValidString();
        verify(saladService).isSaladsEmpty();
        verify(saladService).getSaladsByName("t");
        assertTrue(res.contains("Salads"));
        assertTrue(res.contains(salads.get(1).toString()));
        assertTrue(res.contains(salads.get(2).toString()));
    }

    @Test
    void shouldNotFindSaladsByName() {
        when(inputManager.getValidString()).thenReturn("other salads");
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getSaladsByName("other salads")).thenReturn(List.of());

        getSaladsByNameCommand.execute();
        String res = outputStream.toString();

        verify(inputManager).getValidString();
        verify(saladService).isSaladsEmpty();
        verify(saladService).getSaladsByName("other salads");
        assertTrue(res.contains("Can't find any salads with name"));
        assertFalse(res.contains(salads.get(0).toString()));
        assertFalse(res.contains(salads.get(1).toString()));
        assertFalse(res.contains(salads.get(2).toString()));
    }
}