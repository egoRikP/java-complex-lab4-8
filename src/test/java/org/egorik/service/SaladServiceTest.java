package org.egorik.service;

import org.egorik.model.*;
import org.egorik.repository.MemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SaladServiceTest {

    SaladService saladService;
    List<Product> productList;

    @BeforeEach
    void setUp() {
        saladService = new SaladService(new MemoryRepository<>());

        productList = new ArrayList<>();
        productList.add(new Product("Tomato", 10, 2, 3, 4));
        productList.add(new Vegetable("Onion", 40, 1, 0, 9, "bulb"));
        productList.add(new Vegetable("Cucumber", 15, 1, 0, 3, "fresh"));
        productList.add(new LeafyVegetable("Spinach", 23, 3, 0, 4, "leaf", 91));
        productList.add(new RootVegetable("Carrot", 41, 1, 0, 10, "root", 5));
        productList.add(new Product("Avocado", 160, 2, 15, 9));
        productList.add(new Product("Apple", 54, 34, 5, 9));

        saladService.addSalad(
                new Salad("NewEmptySalad", 25, new ArrayList<>(), new ArrayList<>(), Set.of())
        );

        List<Ingredient> greekIngredients = List.of(
                new Ingredient(productList.get(0), 100.0),
                new Ingredient(productList.get(1), 50.0)
        );
        saladService.addSalad(
                new Salad("GreekSalad", 5, greekIngredients,
                        List.of("Cut vegetables", "Mix gently"),
                        Set.of("fresh", "simple"))
        );


        List<Ingredient> vitaminIngredients = List.of(
                new Ingredient(productList.get(3), 70.0),
                new Ingredient(productList.get(4), 80.0)
        );
        saladService.addSalad(
                new Salad("VitaminSalad", 2, vitaminIngredients,
                        List.of("Shred carrot", "Add spinach", "Stir"),
                        Set.of("healthy", "vitamins"))
        );

        List<Ingredient> mixIngredients = List.of(
                new Ingredient(productList.get(0), 100.0),
                new Ingredient(productList.get(2), 25.0)
        );
        saladService.addSalad(
                new Salad("MixSalad", 6, mixIngredients,
                        List.of("Mix ingredients", "Serve fresh"),
                        Set.of("experimental"))
        );

        List<Ingredient> cucumberBoost = List.of(
                new Ingredient(productList.get(4), 120.0),
                new Ingredient(productList.get(0), 60.0)
        );

        saladService.addSalad(
                new Salad("CucumberBoostSalad", 7, cucumberBoost,
                        List.of("Slice cucumber", "Dice tomato", "Mix"),
                        Set.of("green", "light", "hydrating"))
        );
    }


    @Test
    void shouldAddSalad() {
        int sizeBeforeAdd = saladService.getAllSalads().size();
        saladService.addSalad(new Salad("Salad", 25, new ArrayList<>(), new ArrayList<>(), Set.of()));
        assertEquals(sizeBeforeAdd + 1, saladService.getAllSalads().size());
        assertTrue(saladService.isSaladExists(new Salad("Salad", 25, new ArrayList<>(), new ArrayList<>(), Set.of())));
    }

    @Test
    void shouldNotAddSalad() {
        int sizeBeforeAdd = saladService.getAllSalads().size();
        saladService.addSalad(new Salad("NewEmptySalad", 25, new ArrayList<>(), new ArrayList<>(), Set.of()));
        assertEquals(sizeBeforeAdd, saladService.getAllSalads().size());
    }

    @Test
    void shouldGetAllSalads() {
        assertEquals(5, saladService.getAllSalads().size());
    }

    @Test
    void shouldGetSaladsByName() {
        assertEquals(List.of(saladService.getAllSalads().get(2)), saladService.getSaladsByName("VitaminSalad"));
        assertEquals(saladService.getAllSalads(), saladService.getSaladsByName(" salad "));
    }

    @Test
    void shouldNotGetSaladsByName() {
        assertTrue(saladService.getSaladsByName(" others ").isEmpty());
        assertTrue(saladService.getSaladsByName(" dsasa ").isEmpty());
    }

    @Test
    void shouldGetSaladsByProduct() {
        List<Salad> expectedList = List.of(saladService.getAllSalads().get(1), saladService.getAllSalads().get(3), saladService.getAllSalads().get(4));
        assertEquals(3, saladService.getSaladsByProducts(productList.get(0)).size());
        assertEquals(expectedList, saladService.getSaladsByProducts(productList.get(0)));
    }

    @Test
    void shouldNotGetSaladsByProduct() {
        assertTrue(saladService.getSaladsByProducts(productList.get(5)).isEmpty());
    }

    @Test
    void shouldGetSaladsByProductList() {

        List<Product> toFind = List.of(productList.get(1), productList.get(3));

        List<Salad> expectedList = List.of(saladService.getAllSalads().get(1), saladService.getAllSalads().get(2));

        assertEquals(2, saladService.getSaladsByProducts(toFind).size());
        assertEquals(expectedList, saladService.getSaladsByProducts(toFind));
    }

    @Test
    void shouldNotGetSaladsByProductList() {
        List<Product> toFind = List.of(productList.get(5), productList.get(6));
        assertTrue(saladService.getSaladsByProducts(toFind).isEmpty());
    }

    @Test
    void shouldGetSaladsByTotalCalories() {
        List<Salad> expectedList = List.of(saladService.getAllSalads().get(1), saladService.getAllSalads().get(2));

//        assertEquals(expectedList, saladService.getSaladsByTotalCalories(1, 5));
    }

    @Test
    void shouldNotGetSaladsByTotalCalories() {
        assertTrue(saladService.getSaladsByTotalCalories(10000, 100000).isEmpty());
    }

    @Test
    void shouldGetSaladsByTag() {
        assertEquals(List.of(saladService.getAllSalads().get(2)), saladService.getSaladsByTags("healthy"));
        assertEquals(1, saladService.getSaladsByTags("healthy").size());
    }

    @Test
    void shouldNotGetSaladsByTag() {
        assertTrue(saladService.getSaladsByTags("other").isEmpty());
    }

    @Test
    void shouldGetSaladsByTags() {
        assertEquals(2, saladService.getSaladsByTags(Set.of("healthy", "fresh")).size());
        assertEquals(List.of(saladService.getAllSalads().get(1), saladService.getAllSalads().get(2)), saladService.getSaladsByTags(Set.of("healthy", "fresh")));
    }


    @Test
    void shouldGetSaladsByTimeToFinish() {
        assertEquals(1, saladService.getSaladsByTimeToFinish(25, 30).size());
        assertEquals(List.of(saladService.getAllSalads().get(0)), saladService.getSaladsByTimeToFinish(25, 30));

        assertEquals(2, saladService.getSaladsByTimeToFinish(0, 5).size());
        assertEquals(List.of(saladService.getAllSalads().get(1), saladService.getAllSalads().get(2)), saladService.getSaladsByTimeToFinish(0, 5));
    }

    @Test
    void shouldNotGetSaladsByTimeToFinish() {
        assertTrue(saladService.getSaladsByTimeToFinish(100, 200).isEmpty());
    }

    @Test
    void shouldUpdateSalad() {
        SaladPatch saladPatch = new SaladPatch();
        saladPatch.name = "newName";
        saladPatch.timeToFinish = 50;

        saladPatch.ingredients = List.of(new Ingredient(productList.get(0), 70), new Ingredient(productList.get(0), 50));
        saladPatch.tags = Set.of("easy", "free");
        saladPatch.instruction = List.of("one", "second", "third");


        Salad copy = new Salad(saladService.getAllSalads().get(0));
        saladService.updateSalad(0, saladPatch);
        assertFalse(saladService.isSaladExists(copy));

        assertEquals(saladPatch.name, saladService.getAllSalads().get(0).getName());
        assertEquals(saladPatch.timeToFinish, saladService.getAllSalads().get(0).getTimeToFinish());
        assertEquals(saladPatch.ingredients, saladService.getAllSalads().get(0).getIngredients());
        assertEquals(saladPatch.tags, saladService.getAllSalads().get(0).getTags());
        assertEquals(saladPatch.instruction, saladService.getAllSalads().get(0).getInstruction());
    }

    @Test
    void shouldDeleteSalad() {
        int sizeBeforeDelete = saladService.getAllSalads().size();
        saladService.deleteSalad(0);
        assertFalse(saladService.isSaladExists(new Salad("NewEmptySalad", 25, new ArrayList<>(), new ArrayList<>(), Set.of())));
        assertEquals(sizeBeforeDelete - 1, saladService.getAllSalads().size());
    }

    @Test
    void shouldDeleteAllSalads() {
        int size = saladService.getAllSalads().size();
        for (int i = 0; i < size; i++) {
            saladService.deleteSalad(0);
        }
        assertTrue(saladService.isSaladsEmpty());
    }

    @Test
    void shouldNotDeleteSalad() {
        int sizeBeforeDelete = saladService.getAllSalads().size();
        assertThrows(IndexOutOfBoundsException.class, () -> saladService.deleteSalad(43));
        assertEquals(sizeBeforeDelete, saladService.getAllSalads().size());
    }
}