package org.egorik.command.product;

import org.egorik.command.Command;
import org.egorik.model.Product;
import org.egorik.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GetProductsCommandTest {

    ProductService productService;
    Command getProductsCommand;

    ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        getProductsCommand = new GetProductsCommand(productService);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void shouldGiveEmptyProductList() {
        when(productService.isProductsEmpty()).thenReturn(true);

        getProductsCommand.execute();

        verify(productService).isProductsEmpty();
        assertTrue(outputStream.toString().contains("Product list is empty!"));
    }


    @Test
    void shouldGiveNotEmptyProductList() {
        Product product = new Product("Tomato", 10, 2, 3, 4);
        when(productService.isProductsEmpty()).thenReturn(false);
        when(productService.getAllProducts()).thenReturn(List.of(product));

        getProductsCommand.execute();
        String res = outputStream.toString();

        verify(productService).isProductsEmpty();
        verify(productService).getAllProducts();
        assertTrue(res.contains("All products"));
        assertTrue(res.contains(product.toString()));
    }

}