package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;

import java.util.List;


public interface RecipeService {

    /**
     * Retrieves a list of all recipes.
     *
     * @return a {@link List} of {@link Recipe} objects
     */
    List<Recipe> findAll();

    /**
     * Retrieves a recipe by its unique identifier.
     *
     * @param id the unique identifier of the recipe
     * @return the {@link Recipe} object with the specified ID, or {@code null} if not found
     */
    Recipe findRecipeById(Long id);

    /**
     * Adds a new recipe to the system.
     *
     * @param recipe the {@link Recipe} object to add
     * @return the added {@link Recipe} object
     */
    Recipe addRecipe(Recipe recipe);

    /**
     * Deletes the specified recipe from the system.
     *
     * @param recipe the {@link Recipe} object to delete
     */
    void delete(Recipe recipe);

    /**
     * Finds a recipe by its title.
     *
     * @param title the title of the recipe
     * @return the {@link Recipe} object with the specified title, or {@code null} if not found
     */
    Recipe findByTitle(String title);

    Recipe save(Recipe recipe);

    List<Recipe> searchByKeywordAndCriteria(String keyword, List<DietaryRestriction> selectedDietaryRestrictions, List<MealCategory> selectedMealCategories);

    /**
     * Retrieves a list of all recipes, sorted by their upload timestamp in ascending order.
     *
     * @return a list of {@link Recipe} objects sorted by upload time, from newest to oldest
     */
    List<Recipe> getRecipesSortedByUploadTimeAsc();

    /**
     * Retrieves a list of all recipes, sorted by their upload timestamp in descending order.
     *
     * @return a list of {@link Recipe} objects sorted by upload time, from oldest to newest
     */
    List<Recipe> getRecipesSortedByUploadTimeDesc();
}

