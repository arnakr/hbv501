package is.hi.hbv501g.hopur25.persistence.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")


public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long recipeId;
    private String title;
    private List<String> ingredients = new ArrayList<String>();
    private int cookTime;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(mappedBy = "userFavourites")
    private List<User> usersWhoFavorited = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(String title, List<String> ingredients, int cookTime, String description) {
        this.title = title;
        this.ingredients = ingredients;
        this.cookTime = cookTime;
        this.description = description;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
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

    @Override
    public String toString() {
        return "Title: " + title + ", Ingredients: " + ingredients + ", Cook Time: " + cookTime + ", Description: " + description;
    }

    public void setUser(User currentUser) {
    }
    public List<User> getUsersWhoFavorited() {
        return usersWhoFavorited;
    }

    public void setUsersWhoFavorited(List<User> usersWhoFavorited) {
        this.usersWhoFavorited = usersWhoFavorited;
    }
}

