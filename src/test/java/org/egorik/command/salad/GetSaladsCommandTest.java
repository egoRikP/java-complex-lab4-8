package org.egorik.command.salad;

import org.egorik.command.Command;
import org.egorik.model.Salad;
import org.egorik.service.SaladService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetSaladsCommandTest {

    SaladService saladService;

    Command getSaladsCommand;

    ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        saladService = mock(SaladService.class);
        getSaladsCommand = new GetSaladsCommand(saladService);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptyProductList() {
        when(saladService.isSaladsEmpty()).thenReturn(true);

        getSaladsCommand.execute();

        verify(saladService).isSaladsEmpty();
        assertTrue(outputStream.toString().contains("Salad list is empty!"));
    }


    @Test
    void shouldGiveNotEmptyProductList() {
        Salad salad = new Salad("Tomato", 10, new ArrayList<>(), new ArrayList<>(), new HashSet<>());
        when(saladService.isSaladsEmpty()).thenReturn(false);
        when(saladService.getAllSalads()).thenReturn(List.of(salad));

        getSaladsCommand.execute();
        String res = outputStream.toString();

        verify(saladService).isSaladsEmpty();
        verify(saladService).getAllSalads();
        assertTrue(res.contains("All salads"));
        assertTrue(res.contains(salad.toString()));
    }
}