package org.egorik.model;

import java.util.Objects;
import java.util.function.ToIntFunction;

public class Ingredient {

    private final Product product;
    private double weight;

    public Ingredient(Product product, double weight) {
        this.product = product;
        this.weight = weight;
    }

    public Product getProduct() {
        return product;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTotalBy(ToIntFunction<Product> mapper) {
        return (mapper.applyAsInt(product) * weight / 100);
    }

    public boolean containsProduct(Product product) {
        return this.product.equals(product);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Double.compare(weight, that.weight) == 0 && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, weight);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "product=" + product +
                ", weight=" + weight +
                '}';
    }
}