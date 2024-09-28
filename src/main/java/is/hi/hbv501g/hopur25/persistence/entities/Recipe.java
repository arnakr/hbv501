package is.hi.hbv501g.hopur25.persistence.entities;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private long recipeID;
    private String title;
    private List<String> ingredients = new ArrayList<String>();
    private int cookTime;
    private String description;

    public Recipe() {
    }

    public Recipe(String title, List<String> ingredients, int cookTime, String description) {
        this.title = title;
        this.ingredients = ingredients;
        this.cookTime = cookTime;
        this.description = description;
    }

    public long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(long recipeID) {
        this.recipeID = recipeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

