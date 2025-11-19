package org.egorik.model;

public class RootVegetable extends Vegetable {
    private int starchContent;

    public RootVegetable(String name, int caloriesPer100, int proteinsPer100, int fatsPer100, int carbsPer100, String type, int starchContent) {
        super(name, caloriesPer100, proteinsPer100, fatsPer100, carbsPer100, type);
        this.starchContent = starchContent;
    }

    public RootVegetable(RootVegetable rootVegetable) {
        super(rootVegetable);
        this.starchContent = rootVegetable.getStarchContent();
    }

    public int getStarchContent() {
        return starchContent;
    }

    public void setStarchContent(int starchContent) {
        this.starchContent = starchContent;
    }

    public int getBaseCalories() {
        return caloriesPer100;
    }

    @Override
    public int getCaloriesPer100() {
        return super.getCaloriesPer100() + starchContent * 4;
    }

    @Override
    public String toString() {
        return "RootVegetable{" +
                "starchContent=" + starchContent +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", caloriesPer100=" + getCaloriesPer100() +
                ", BaseCaloriesPer100=" + getBaseCalories() +
                ", proteinsPer100=" + proteinsPer100 +
                ", fatsPer100=" + fatsPer100 +
                ", carbsPer100=" + carbsPer100 +
                '}';
    }

}