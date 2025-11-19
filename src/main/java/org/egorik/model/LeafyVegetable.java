package org.egorik.model;

public class LeafyVegetable extends Vegetable {

    private int waterPercentage;

    public LeafyVegetable(String name, int caloriesPer100, int proteinsPer100, int fatsPer100, int carbsPer100, String type, int waterPercentage) {
        super(name, caloriesPer100, proteinsPer100, fatsPer100, carbsPer100, type);
        this.waterPercentage = waterPercentage;
    }

    public LeafyVegetable(LeafyVegetable leafyVegetable) {
        super(leafyVegetable);
        this.waterPercentage = leafyVegetable.getWaterPercentage();
    }

    public int getWaterPercentage() {
        return waterPercentage;
    }

    public void setWaterPercentage(int waterPercentage) {
        this.waterPercentage = waterPercentage;
    }

    @Override
    public String toString() {
        return "LeafyVegetable{" +
                "waterPercentage=" + waterPercentage +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", caloriesPer100=" + caloriesPer100 +
                ", proteinsPer100=" + proteinsPer100 +
                ", fatsPer100=" + fatsPer100 +
                ", carbsPer100=" + carbsPer100 +
                '}';
    }

}