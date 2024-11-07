package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;

import java.util.List;


public interface RecipeService {
    /**
     * Retrieves a recipe by its unique identifier.
     *
     * @param id the unique identifier of the recipe
     * @return the {@link Recipe} object with the specified ID, or {@code null} if not found
     */
    Recipe findRecipeById(Long id);


    /**
     * Deletes the specified recipe from the system.
     *
     * @param recipeId the {@link Recipe} object to delete
     */
    void delete(Long recipeId, User currentUser);


    /**
     * Saves the recipe to the database.
     *
     * @param recipe to save
     */
    Recipe save(Recipe recipe);

    List<Recipe> searchByKeywordAndCriteria(String keyword, List<DietaryRestriction> selectedDietaryRestrictions, List<MealCategory> selectedMealCategories);

    void updateRecipe(Recipe updatedRecipe);
}

