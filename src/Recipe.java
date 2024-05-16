import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private String currency;
    private List<Ingredients> ingredients;
    private double cost;

    public Recipe(String name, String currency) {
        this.name = name;
        this.currency = currency;
        ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredients ingredient) {
        ingredients.add(ingredient);
    }

    public double calculateCost() {
        double totalCost = 0;
        for (Ingredients ingredient : ingredients) {
            totalCost += ingredient.getPrice();
        }
        return totalCost;
    }

    public int ingredientsCount() {
        return ingredients.size();
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }
}
