package is.hi.hbv501g.hopur25.persistence.entities;

import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A model object for recipes. Contains all relevant information, getters and setters.
 */
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeId;

    private String title;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients")
    @Column(name = "ingredient")
    private List<String> ingredients = new ArrayList<>();

    private String description;

    private int cookTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_dietary_restrictions")
    @Column(name = "dietary_restriction")
    @Enumerated(EnumType.STRING)
    private Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "recipe_meal_categories")
    @Column(name = "meal_category")
    @Enumerated(EnumType.STRING)
    private Set<MealCategory> mealCategories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(mappedBy = "userFavourites")
    private List<User> usersWhoFavorited = new ArrayList<>();

    /* Constructors */
    public Recipe() {
    }

    public Recipe(String title, List<String> ingredients, String description, int cookTime, Set<DietaryRestriction> dietaryRestrictions, Set<MealCategory> mealCategories, User user, List<User> usersWhoFavorited) {
        this.title = title;
        this.ingredients = ingredients;
        this.description = description;
        this.cookTime = cookTime;
        this.dietaryRestrictions = dietaryRestrictions;
        this.mealCategories = mealCategories;
        this.user = user;
        this.usersWhoFavorited = usersWhoFavorited;
    }

    /* Getters and setters */
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public Set<DietaryRestriction> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(Set<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public Set<MealCategory> getMealCategories() {
        return mealCategories;
    }

    public void setMealCategories(Set<MealCategory> mealCategories) {
        this.mealCategories = mealCategories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsersWhoFavorited() {
        return usersWhoFavorited;
    }

    public void setUsersWhoFavorited(List<User> usersWhoFavorited) {
        this.usersWhoFavorited = usersWhoFavorited;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Ingredients: " + ingredients + ", Cook Time: " + cookTime + ", Description: " + description;
    }


}

