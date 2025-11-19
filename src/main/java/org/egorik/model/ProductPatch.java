package org.egorik.model;

public class ProductPatch {
    public String name;
    public Integer caloriesPer100;
    public Integer proteinsPer100;
    public Integer fatsPer100;
    public Integer carbsPer100;

    public String type;
    public Integer waterPercentage;
    public Integer starchContent;

    public void updateProduct(Product product) {
        if (name != null) {
            product.setName(name);
        }
        if (caloriesPer100 != null) {
            product.setCaloriesPer100(caloriesPer100);
        }
        if (proteinsPer100 != null) {
            product.setProteinsPer100(proteinsPer100);
        }
        if (fatsPer100 != null) {
            product.setFatsPer100(fatsPer100);
        }
        if (carbsPer100 != null) {
            product.setCarbsPer100(carbsPer100);
        }

        if (product instanceof Vegetable veg && type != null) {
            veg.setType(type);
        }

        if (product instanceof LeafyVegetable leafy && waterPercentage != null) {
            leafy.setWaterPercentage(waterPercentage);
        }

        if (product instanceof RootVegetable root && starchContent != null) {
            root.setStarchContent(starchContent);
        }
    }
}
