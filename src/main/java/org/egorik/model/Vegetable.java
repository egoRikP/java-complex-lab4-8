package org.egorik.model;

public class Vegetable extends Product {

    protected String type;

    public Vegetable(String name, int caloriesPer100, int proteinsPer100, int fatsPer100, int carbsPer100, String type) {
        super(name, caloriesPer100, proteinsPer100, fatsPer100, carbsPer100);
        this.type = type;
    }

    public Vegetable(Vegetable vegetable) {
        super(vegetable);
        this.type = vegetable.getType();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Vegetable{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", caloriesPer100=" + caloriesPer100 +
                ", proteinsPer100=" + proteinsPer100 +
                ", fatsPer100=" + fatsPer100 +
                ", carbsPer100=" + carbsPer100 +
                '}';
    }
}