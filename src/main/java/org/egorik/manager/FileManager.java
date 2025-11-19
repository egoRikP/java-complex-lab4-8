package org.egorik.manager;

import org.egorik.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FileManager {

    public final String DEFAULT_PRODUCTS_PATH = "src/main/resources/products.txt";
    public final String DEFAULT_SALADS_PATH = "src/main/resources/salads.txt";

    public List<Product> loadProducts(String path) throws FileNotFoundException {
        List<Product> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String buff;
            int lineNumber = 1;

            while ((buff = reader.readLine()) != null) {
                try {
                    result.add(getProductFromString(buff));
                } catch (Exception e) {
                    System.out.printf("Error in product file at line %d: %s\nContent: %s\n",
                            lineNumber, e.getMessage(), buff);
                }
                lineNumber++;
            }

        } catch (IOException ioException) {
            throw new FileNotFoundException(String.format("Can't find product file - %s", path));
        }
        return result;
    }

    public List<Salad> loadSalads(String path, List<Product> products) throws FileNotFoundException {
        List<Salad> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String buff;
            int lineNumber = 1;

            while ((buff = reader.readLine()) != null) {
                try {
                    result.add(getSaladFromString(buff, products));
                } catch (Exception e) {
                    System.out.printf("Error in salad file at line %d: %s\n%s\n", lineNumber, buff, e.getMessage());
                }
                lineNumber++;
            }

        } catch (IOException e) {
            throw new FileNotFoundException("Can't find salad file: " + path);
        }
        return result;
    }

    public void saveProducts(List<Product> products, String path) {

        File file = new File(path);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        if (!products.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {

                for (Product product : products) {
                    writer.write(getProductAsString(product));
                    writer.newLine();
                }

            } catch (IOException ioException) {
                System.out.printf("Error saving products file '%s': %s\n", path, ioException.getMessage());
            }
        }

    }

    public void saveSalads(List<Salad> salads, String path) {

        File file = new File(path);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        if (!salads.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {

                for (Salad salad : salads) {
                    writer.write(getSaladAsString(salad));
                    writer.newLine();
                }

            } catch (IOException ioException) {
                System.out.printf("Error saving salads file '%s': %s\n", path, ioException.getMessage());
            }
        }
    }

    public String getProductAsString(Product product) {
        if (product instanceof LeafyVegetable leafy) {
            return String.format(
                    "LEAFY,%s,%d,%d,%d,%d,%s,%d",
                    leafy.getName(),
                    leafy.getCaloriesPer100(),
                    leafy.getProteinsPer100(),
                    leafy.getFatsPer100(),
                    leafy.getCarbsPer100(),
                    leafy.getType(),
                    leafy.getWaterPercentage()
            );
        }

        if (product instanceof RootVegetable root) {
            return String.format(
                    "ROOT,%s,%d,%d,%d,%d,%s,%d",
                    root.getName(),
                    root.getBaseCalories(),
                    root.getProteinsPer100(),
                    root.getFatsPer100(),
                    root.getCarbsPer100(),
                    root.getType(),
                    root.getStarchContent()
            );
        }

        if (product instanceof Vegetable veg) {
            return String.format(
                    "VEGETABLE,%s,%d,%d,%d,%d,%s",
                    veg.getName(),
                    veg.getCaloriesPer100(),
                    veg.getProteinsPer100(),
                    veg.getFatsPer100(),
                    veg.getCarbsPer100(),
                    veg.getType()
            );
        }

        return String.format(
                "PRODUCT,%s,%d,%d,%d,%d",
                product.getName(),
                product.getCaloriesPer100(),
                product.getProteinsPer100(),
                product.getFatsPer100(),
                product.getCarbsPer100()
        );
    }

    private int parseInt(String value, String field) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(field + " must be a number!");
        }
    }

    public Product getProductFromString(String line) {

        if (line.isBlank()) {
            throw new IllegalArgumentException("Not empty line!");
        }

        List<String> res = Arrays.stream(line.split(",")).map(String::trim).toList();
        String type = res.get(0).toUpperCase();

        if (!List.of("PRODUCT", "VEGETABLE", "LEAFY", "ROOT").contains(type) || res.size() < 6) {
            throw new IllegalArgumentException("Not valid product type!");
        }

        String name = res.get(1);
        int cal = parseInt(res.get(2), "Calories");
        int prot = parseInt(res.get(3), "Proteins");
        int fat = parseInt(res.get(4), "Fats");
        int carb = parseInt(res.get(5), "Carbs");

        return switch (type) {
            case "PRODUCT" -> new Product(name, cal, prot, fat, carb);
            case "VEGETABLE" -> {
                if (res.size() != 7) {
                    throw new IllegalArgumentException("Not valid product type!");
                }

                yield new Vegetable(name, cal, prot, fat, carb, res.get(6).trim());
            }
            case "LEAFY" -> {

                if (res.size() != 8) {
                    throw new IllegalArgumentException("Not valid product type!");
                }

                int water = parseInt(res.get(7).trim(), "Water");
                yield new LeafyVegetable(name, cal, prot, fat, carb, res.get(6).trim(), water);
            }
            case "ROOT" -> {

                if (res.size() != 8) {
                    throw new IllegalArgumentException("Not valid product type!");
                }


                int starch = parseInt(res.get(7).trim(), "Starch");
                yield new RootVegetable(name, cal, prot, fat, carb, res.get(6).trim(), starch);
            }
            default -> throw new IllegalArgumentException("Not a valid");
        };

    }

    public String getSaladAsString(Salad salad) {
        String ingredients = salad.getIngredients().stream()
                .map(i -> i.getProduct().getName() + ":" + i.getWeight())
                .collect(Collectors.joining(";"));

        String tags = String.join(";", salad.getTags());

        String instructions = String.join(";", salad.getInstruction());

        return String.format(
                "%s|%s|%s|%s|%d",
                salad.getName(),
                ingredients,
                tags,
                instructions,
                salad.getTimeToFinish()
        );
    }

    public Salad getSaladFromString(String string, List<Product> products) {

        List<String> res = List.of(string.split("\\|"));

        if (res.size() != 5) {
            throw new IllegalArgumentException("Salad must have 5 fields: name|ing|tags|steps|time");
        }

        String name = res.getFirst();

        List<Ingredient> ingredientList = new ArrayList<>();
        List<String> ingredientsString = List.of(res.get(1).trim().split(";"));

        for (String ingredient : ingredientsString) {

            List<String> a = List.of(ingredient.trim().split(":"));

            if (a.getFirst().isBlank()) {
                continue;
            }

            if (!a.isEmpty() && a.size() != 2) {
                System.out.println("Invalid ingredient format: " + ingredient);
                continue;
            }

            Product product = products.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(a.getFirst().trim()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found: " + a.getFirst()));

            double weight;
            try {
                weight = Double.parseDouble(a.get(1));
            } catch (NumberFormatException numberFormatException) {
                throw new NumberFormatException("Weight must be number!");
            }

            ingredientList.add(new Ingredient(product, weight));
        }

        Set<String> tags = Arrays.stream(res.get(2).trim().split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());


        List<String> instructionList = Arrays.stream(res.get(3).split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        int timeToFinish = parseInt(res.get(4).trim(), "time to finish");
        return new Salad(name, timeToFinish, ingredientList, instructionList, tags);
    }
}