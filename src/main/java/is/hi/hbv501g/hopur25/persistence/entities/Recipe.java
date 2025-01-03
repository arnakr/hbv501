package is.hi.hbv501g.hopur25.persistence.entities;

import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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

    private String recipePictureUrl;
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
    @JoinColumn(name = "user_userid")
    private User user;

    @ManyToMany(mappedBy = "userFavourites")
    private List<User> usersWhoFavorited = new ArrayList<>();

    private LocalDateTime uploadTime = LocalDateTime.now();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = true)
    private Double avgRating = 0.0;

    /* Constructors */
    public Recipe() {
    }

    public Recipe(String title, List<String> ingredients, String description, int cookTime,
                  Set<DietaryRestriction> dietaryRestrictions, Set<MealCategory> mealCategories,
                  User user, List<User> usersWhoFavorited, String recipePictureUrl, List<Review> reviews) {
        this.title = title;
        this.ingredients = ingredients;
        this.description = description;
        this.cookTime = cookTime;
        this.dietaryRestrictions = dietaryRestrictions;
        this.mealCategories = mealCategories;
        this.user = user;
        this.usersWhoFavorited = usersWhoFavorited;
        this.uploadTime = LocalDateTime.now();
        this.recipePictureUrl = recipePictureUrl;
        this.reviews = reviews;
    }

    /* Getters and setters */
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

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getRecipePictureUrl() {
        return recipePictureUrl;
    }

    public void setRecipePictureUrl(String recipePictureUrl) {
        this.recipePictureUrl = recipePictureUrl;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    @Override
    public String toString() {
        return "Id: " + recipeId + ", Title: " + title + ", Ingredients: " + ingredients + ", Cook Time: " + cookTime + ", Description: " + description;
    }
}