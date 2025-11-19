package org.egorik.model;

import java.util.Objects;

public class Product {

    protected String name;
    protected int caloriesPer100;
    protected int proteinsPer100;
    protected int fatsPer100;
    protected int carbsPer100;

    public Product(String name, int caloriesPer100, int proteinsPer100, int fatsPer100, int carbsPer100) {
        this.name = name;
        this.caloriesPer100 = caloriesPer100;
        this.proteinsPer100 = proteinsPer100;
        this.fatsPer100 = fatsPer100;
        this.carbsPer100 = carbsPer100;
    }

    public Product(Product product) {
        this.name = product.name;
        this.caloriesPer100 = product.caloriesPer100;
        this.proteinsPer100 = product.proteinsPer100;
        this.fatsPer100 = product.fatsPer100;
        this.carbsPer100 = product.carbsPer100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCaloriesPer100() {
        return caloriesPer100;
    }

    public void setCaloriesPer100(int caloriesPer100) {
        this.caloriesPer100 = caloriesPer100;
    }

    public int getProteinsPer100() {
        return proteinsPer100;
    }

    public void setProteinsPer100(int proteinsPer100) {
        this.proteinsPer100 = proteinsPer100;
    }

    public int getFatsPer100() {
        return fatsPer100;
    }

    public void setFatsPer100(int fatsPer100) {
        this.fatsPer100 = fatsPer100;
    }

    public int getCarbsPer100() {
        return carbsPer100;
    }

    public void setCarbsPer100(int carbsPer100) {
        this.carbsPer100 = carbsPer100;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return caloriesPer100 == product.caloriesPer100 && proteinsPer100 == product.proteinsPer100 && fatsPer100 == product.fatsPer100 && carbsPer100 == product.carbsPer100 && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, caloriesPer100, proteinsPer100, fatsPer100, carbsPer100);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", caloriesPer100=" + caloriesPer100 +
                ", proteinsPer100=" + proteinsPer100 +
                ", fatsPer100=" + fatsPer100 +
                ", carbsPer100=" + carbsPer100 +
                '}';
    }
}