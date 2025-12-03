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

class GetSaladsByTagSetCommandTest {

    SaladService saladService;
    InputManager inputManager;

    Command getSaladsByTagSetCommand;

    ByteArrayOutputStream outputStream;

    List<Salad> salads = List.of(
            new Salad("GreekSalad", 5, List.of(), List.of(), Set.of("easy newyear")),
            new Salad("VitaminSalad", 2, List.of(), List.of(), Set.of("fresh")),
            new Salad("BoostSalad", 7, List.of(), List.of(), Set.of("newyear fresh"))
    );

    @BeforeEach
    void setUp() {
        saladService = mock(SaladService.class);
        inputManager = mock(InputManager.class);
        getSaladsByTagSetCommand = new GetSaladsByTagSetCommand(saladService, inputManager);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptySaladList() {
        when(saladService.isSaladsEmpty()).thenReturn(true);

        getSaladsByTagSetCommand.execute();

        verify(saladService).isSaladsEmpty();
        assertTrue(outputStream.toString().contains("Salad list is empty!"));
    }

    @Test
    void shouldFindSaladsByTagSet() {
        when(inputManager.getString()).thenReturn("newyear");
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getSaladsByTags(Set.of("newyear"))).thenReturn(List.of(salads.get(0), salads.get(2)));

        getSaladsByTagSetCommand.execute();
        String res = outputStream.toString();

        verify(inputManager).getString();
        verify(saladService).isSaladsEmpty();
        verify(saladService).getSaladsByTags(Set.of("newyear"));
        assertTrue(res.contains("Salads with tags"));
        assertTrue(res.contains(salads.get(0).toString()));
        assertTrue(res.contains(salads.get(2).toString()));
    }

    @Test
    void shouldNotFindSaladsByTagSet() {
        when(inputManager.getString()).thenReturn("t");
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getSaladsByTags(Set.of("t"))).thenReturn(List.of());

        getSaladsByTagSetCommand.execute();
        String res = outputStream.toString();

        verify(inputManager).getString();
        verify(saladService).isSaladsEmpty();
        verify(saladService).getSaladsByTags(Set.of("t"));
        assertTrue(res.contains("Empty"));
        assertFalse(res.contains(salads.get(0).toString()));
        assertFalse(res.contains(salads.get(1).toString()));
        assertFalse(res.contains(salads.get(2).toString()));
    }
}