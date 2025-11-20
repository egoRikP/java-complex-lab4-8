package org.egorik.manager;

import org.egorik.model.LeafyVegetable;
import org.egorik.model.Product;
import org.egorik.model.RootVegetable;
import org.egorik.model.Vegetable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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


}