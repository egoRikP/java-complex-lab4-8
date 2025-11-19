package org.egorik.model;

import java.util.List;
import java.util.Set;

public class SaladPatch {
    public String name;
    public Integer timeToFinish;
    public List<Ingredient> ingredients;
    public List<String> instruction;
    public Set<String> tags;

    public void updateSalad(Salad salad) {
        if (name != null) {
            salad.setName(name);
        }
        if (timeToFinish != null) {
            salad.setTimeToFinish(timeToFinish);
        }
        if (ingredients != null) {
            salad.setIngredients(ingredients);
        }
        if (instruction != null) {
            salad.setInstruction(instruction);
        }
        if (tags != null) {
            salad.setTags(tags);
        }
    }

}