package org.egorik.model;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class Salad {

    private String name;
    private int timeToFinish;
    private List<Ingredient> ingredients;
    private List<String> instruction;
    private Set<String> tags;


    public Salad(String name, int timeToFinish, List<Ingredient> ingredients, List<String> instruction, Set<String> tags) {
        this.name = name;
        this.timeToFinish = timeToFinish;
        this.ingredients = ingredients;
        this.instruction = instruction;
        this.tags = tags;
    }

    public Salad(Salad other) {
        this.name = other.name;
        this.timeToFinish = other.timeToFinish;
        this.ingredients = other.ingredients.stream()
                .map(i -> new Ingredient(i.getProduct(), i.getWeight())).toList();
        this.instruction = new ArrayList<>(other.instruction);
        this.tags = Set.copyOf(other.tags);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeToFinish() {
        return timeToFinish;
    }

    public void setTimeToFinish(int timeToFinish) {
        this.timeToFinish = timeToFinish;
    }

    public List<Ingredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstruction() {
        return Collections.unmodifiableList(instruction);
    }

    public void setInstruction(List<String> instruction) {
        this.instruction = instruction;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public double getTotalBy(ToIntFunction<Product> mapper) {
        return ingredients.stream()
                .mapToDouble(ingredient -> ingredient.getTotalBy(mapper))
                .sum();
    }

    public boolean containsProduct(Product product) {
        return ingredients.stream()
                .anyMatch(ingredient -> ingredient.containsProduct(product));
    }

    public boolean containsProduct(List<Product> products) {
        return products.stream().anyMatch(this::containsProduct);
    }

    public boolean containsTag(String tag) {
        return this.tags.contains(tag);
    }

    public boolean containsTag(Set<String> tags) {
        return tags.stream().anyMatch(this::containsTag);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Salad salad = (Salad) o;
        return timeToFinish == salad.timeToFinish && Objects.equals(ingredients, salad.ingredients) && Objects.equals(instruction, salad.instruction) && Objects.equals(tags, salad.tags) && Objects.equals(name, salad.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredients, instruction, tags, name, timeToFinish);
    }

    public String detailedInfo() {

        String ingredientsStr = (ingredients == null || ingredients.isEmpty())
                ? "(no ingredients)"
                : ingredients.stream()
                .map(i -> "    - " + i.getProduct().getName()
                        + " (" + i.getWeight() + "g, "
                        + String.format("%.2f", i.getTotalBy(Product::getCaloriesPer100)) + " kcal)")
                .collect(Collectors.joining("\n"));

        String tagsStr = (tags == null || tags.isEmpty() || tags.contains(""))
                ? "(none)"
                : String.join(", ", tags);

        String instructionStr = (instruction == null || instruction.isEmpty())
                ? "(no steps)"
                : instruction.stream()
                .filter(step -> !step.isBlank())
                .map(step -> "    - " + step)
                .collect(Collectors.joining("\n"));

        if (instructionStr.isEmpty()) {
            instructionStr = "(no steps)";
        }

        return """
                Salad: %s
                  Time: %d min
                  Total calories: %.1f kcal
                  Ingredients:
                  %s
                  Tags: %s
                  Steps:
                  %s
                """.formatted(
                name,
                timeToFinish,
                getTotalBy(Product::getCaloriesPer100),
                ingredientsStr,
                tagsStr,
                instructionStr
        );
    }


    @Override
    public String toString() {
        return "Salad{" +
                "name='" + name + '\'' +
                ", timeToFinish=" + timeToFinish +
                ", ingredients=" + ingredients.stream().map(ingredient -> ingredient + ":" + ingredient.getWeight()).collect(Collectors.joining(";")) +
                ", instruction=" + String.join(";", instruction) +
                ", tags=" + String.join(";", tags) +
                '}';
    }
}