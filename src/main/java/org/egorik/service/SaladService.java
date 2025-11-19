package org.egorik.service;

import org.egorik.model.Product;
import org.egorik.model.Salad;
import org.egorik.model.SaladPatch;
import org.egorik.repository.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SaladService {

    private final Repository<Salad> repository;

    public SaladService(Repository<Salad> repository) {
        this.repository = repository;
    }

    public boolean isSaladsEmpty() {
        return repository.isEmpty();
    }

    public boolean isSaladExists(Salad salad) {
        return getAllSalads().contains(salad);
    }

    public void addSalad(Salad newSalad) {
        if (isSaladExists(newSalad)) {
            System.out.printf("Salad - %s - exists!\n", newSalad);
            return;
        }
        repository.add(newSalad);
    }

    public List<Salad> getAllSalads() {
        return Collections.unmodifiableList(repository.getAll());
    }

    public void updateSalad(int ind, SaladPatch saladPatch) {
        saladPatch.updateSalad(repository.getAll().get(ind));
    }

    public void deleteSalad(int ind) {
        repository.delete(ind);
    }

    private List<Salad> filter(Predicate<Salad> predicate) {
        return repository.getAll().stream().filter(predicate).toList();
    }

    public List<Salad> getSaladsByName(String name) {
        return filter(salad -> salad.getName().toLowerCase().contains(name.trim().toLowerCase()));
    }

    public List<Salad> getSaladsByProducts(List<Product> productList) {
        return filter(salad -> salad.containsProduct(productList));
    }

    public List<Salad> getSaladsByProducts(Product product) {
        return filter(salad -> salad.containsProduct(product));
    }

    public List<Salad> getSaladsByTotalCalories(int min, int max) {
        return filter(salad -> {
            double value = salad.getTotalBy(Product::getCaloriesPer100);
            return value >= min && value <= max;
        });
    }

    public List<Salad> getSaladsByTags(Set<String> tags) {
        return filter(salad -> salad.containsTag(tags));
    }

    public List<Salad> getSaladsByTags(String tag) {
        return filter(salad -> salad.containsTag(tag));
    }

    public List<Salad> getSaladsByTimeToFinish(int minTime, int maxTime) {
        return filter(salad -> {
            int timeToFinish = salad.getTimeToFinish();
            return timeToFinish >= minTime && timeToFinish <= maxTime;
        });
    }

}