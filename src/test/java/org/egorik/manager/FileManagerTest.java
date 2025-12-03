package org.egorik.manager;

import org.egorik.model.*;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    FileManager fileService = new FileManager();

    @Test
    void shouldReadAsProduct() {
        Product expectedProduct = new Product("Name", 1, 2, 3, 4);

        assertEquals(expectedProduct, fileService.getProductFromString("PRODUCT,Name,1,2,3,4"));
        assertEquals(expectedProduct, fileService.getProductFromString("PRODUCT,           Name,1,2            ,3,4"));
        assertEquals(expectedProduct, fileService.getProductFromString("       PRODUCT,Name,1      ,2,3,4       "));
        assertEquals(expectedProduct, fileService.getProductFromString("       PRODUCT,         Name,1      ,2,             3       ,4       "));
        assertEquals(expectedProduct, fileService.getProductFromString("       prodUCT,         Name,1      ,2,             3       ,4       "));
    }

    @Test
    void shouldNotReadAsProduct() {
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("PRODUCT,Name,f,2,3,4"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("PRODUCT,Name,1,a,.3,4"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("PRODUCT,Name,f,2,a,f"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("PRODUCT,Name,f,2,3,a"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("sdf,Name,1,2,3,4"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("Name,f,2,3,a"));
    }

    @Test
    void shouldReadAsVegetable() {
        Product expectedProduct = new Vegetable("Cucumber", 15, 1, 0, 3, "green");

        assertEquals(expectedProduct, fileService.getProductFromString("VEGETABLE,Cucumber,15,1,0,3,green"));
        assertEquals(expectedProduct, fileService.getProductFromString("VEGETABLE   ,   Cucumber,   15,     1,  0       ,3,green"));
        assertEquals(expectedProduct, fileService.getProductFromString("VEGETABLE           ,Cucumber,  15,1,0,3,            green"));
        assertEquals(expectedProduct, fileService.getProductFromString("VEGETABLE,Cucumber,15               ,1      ,0      ,   3            ,green"));
        assertEquals(expectedProduct, fileService.getProductFromString("VEgETABLE,Cucumber,15               ,1      ,0      ,   3            ,green"));
    }

    @Test
    void shouldNotReadAsVegetable() {
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("VEGETABLE,Cucumber,f,1,0,3,green"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("VEGETABLE,Cucumber,15,f,0,3,green"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("VEGETABLE,Cucumber,15,1,f,3,green"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("VEGETABLE,Cucumber,15,1,0,f,green"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("VEGETABLE,Cucumber,15,1,0,3"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("dsa,Cucumber,15,1,0,3"));
    }

    @Test
    void shouldReadAsRootVegetable() {
        Product expectedProduct = new RootVegetable("Carrot", 41, 1, 0, 10, "root", 20);

        assertEquals(expectedProduct, fileService.getProductFromString("ROOT,Carrot,41,1,0,10,root,20"));
        assertEquals(expectedProduct, fileService.getProductFromString("ROOT            ,Carrot,    41,     1,      0,10        ,root,  20"));
        assertEquals(expectedProduct, fileService.getProductFromString("        ROOT,Carrot,41,1            ,0,10,root,         20"));
        assertEquals(expectedProduct, fileService.getProductFromString("ROOT,   Carrot, 41,1    ,0,10,  root,   20"));
        assertEquals(expectedProduct, fileService.getProductFromString("rOOt        ,Carrot,41,1,0,10,root,20"));
    }

    @Test
    void shouldNotReadAsRootVegetable() {
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("ROOT,Carrot,f,1,0,10,root,20"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("ROOT,Carrot,41,f,0,10,root,20"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("ROOT,Carrot,41,1,f,10,root,20"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("ROOT,Carrot,41,1,0,f,root,20"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("ROOT,Carrot,41,1,0,10,root,f"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("ROOT"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString(",Carrot,41,1,0,10,20"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("Rf,Carrot,41,1,0,10,20"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("Carrot,41,1,0,10,20"));
    }

    @Test
    void shouldReadAsLeafyVegetable() {
        Product expectedProduct = new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 92);
        assertEquals(expectedProduct, fileService.getProductFromString("LEAFY,Spinach, 23, 3, 0, 4, leaf, 92"));
        assertEquals(expectedProduct, fileService.getProductFromString("LEAFY       ,Spinach        ,23     ,3      ,0      ,4  ,leaf,92"));
        assertEquals(expectedProduct, fileService.getProductFromString("LEAFY,Spinach,23,3,     0,4,leaf     ,92    "));
        assertEquals(expectedProduct, fileService.getProductFromString("LEAFY,      Spinach,23,3,0,4,leaf,92"));
        assertEquals(expectedProduct, fileService.getProductFromString("LEAFY,Spinach,23,3,0,4,leaf,92"));
        assertEquals(expectedProduct, fileService.getProductFromString("LEAFY,Spinach,23,3,0,4,leaf,92"));
    }

    @Test
    void shouldNotReadAsLeafyVegetable() {
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("LEAFY,Spinach, f, 3, 0, 4, leaf, 92"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("LEAFY,Spinach, 23, f, 0, 4, leaf, 92"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("LEAFY,Spinach, 23, 3, f, 4, leaf, 92"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("LEAFY,Spinach, 23, 3, 0, f, leaf, 92"));
        assertThrows(NumberFormatException.class, () -> fileService.getProductFromString("LEAFY,Spinach, 23, 3, 0, 4, leaf, f"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("sda,Spinach, 23, 3, 0, 4, 92"));
        assertThrows(IllegalArgumentException.class, () -> fileService.getProductFromString("sda,Spinach, 23, 3, 0, "));
    }

    @Test
    void shouldSaveProductToString() {
        Product p = new Product("Apple", 52, 0, 0, 14);
        String s = fileService.getProductAsString(p);

        assertEquals("PRODUCT,Apple,52,0,0,14", s);
    }

    @Test
    void shouldSaveVegetableToString() {
        Product p = new Vegetable("Tomato", 18, 1, 0, 4, "fruit");
        String s = fileService.getProductAsString(p);

        assertEquals("VEGETABLE,Tomato,18,1,0,4,fruit", s);
    }

    @Test
    void shouldSaveLeafyToString() {
        Product p = new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 92);
        String s = fileService.getProductAsString(p);

        assertEquals("LEAFY,Spinach,23,3,0,4,leaf,92", s);
    }

    @Test
    void shouldSaveRootToString() {
        Product p = new RootVegetable("Carrot", 41, 1, 0, 10, "root", 20);
        String s = fileService.getProductAsString(p);

        assertEquals("ROOT,Carrot,41,1,0,10,root,20", s);
    }

    @Test
    void shouldReadSalad() {
        List<Product> products = List.of(
                new Product("Apple", 52, 0, 0, 14),
                new Product("Banana", 89, 1, 0, 23)
        );
        String line = "FruitMix|Apple:100;Banana:50|sweet;light|cut;mix;serve|5";
        Salad s = fileService.getSaladFromString(line, products);

        assertEquals("FruitMix", s.getName());
        assertEquals(5, s.getTimeToFinish());
        assertEquals(2, s.getIngredients().size());
        assertEquals("Apple", s.getIngredients().get(0).getProduct().getName());
        assertEquals(100.0, s.getIngredients().get(0).getWeight());
        assertEquals("Banana", s.getIngredients().get(1).getProduct().getName());
        assertTrue(s.getTags().contains("sweet"));
        assertTrue(s.getTags().contains("light"));
        assertEquals(List.of("cut", "mix", "serve"), s.getInstruction());
    }

    @Test
    void shouldReadSaladWithEmptyIngredients() {
        List<Product> products = List.of();
        String line = "EmptySalad||fresh|just do it|3";

        Salad s = fileService.getSaladFromString(line, products);

        assertEquals("EmptySalad", s.getName());
        assertTrue(s.getIngredients().isEmpty());
        assertEquals(Set.of("fresh"), s.getTags());
        assertEquals(List.of("just do it"), s.getInstruction());
        assertEquals(3, s.getTimeToFinish());
    }

    @Test
    void shouldFailSaladBadFormat() {
        List<Product> pr = List.of();
        assertThrows(IllegalArgumentException.class,
                () -> fileService.getSaladFromString("bad line", pr));
    }

    @Test
    void shouldFailIfIngredientProductMissing() {
        List<Product> products = List.of(
                new Product("Apple", 52, 0, 0, 14)
        );

        String bad = "Mix|Banana:10|x|y|5";
        assertThrows(RuntimeException.class,
                () -> fileService.getSaladFromString(bad, products));
    }

    @Test
    void shouldSaveSaladToString() {
        List<Ingredient> ing = List.of(
                new Ingredient(new Product("Apple", 52, 0, 0, 14), 100),
                new Ingredient(new Product("Banana", 89, 1, 0, 23), 50)
        );

        Set<String> a = new LinkedHashSet<>();
        a.addAll(List.of("sweet", "fresh"));

        Salad s = new Salad(
                "FruitMix", 5,
                ing,
                List.of("cut", "mix"),
                a
        );

        String line = fileService.getSaladAsString(s);

        assertTrue(line.startsWith("FruitMix|Apple:100.0;Banana:50.0|"));
        assertTrue(line.endsWith("|cut;mix|5"));

        assertTrue(line.contains("fresh"));
        assertTrue(line.contains("sweet"));
    }
}