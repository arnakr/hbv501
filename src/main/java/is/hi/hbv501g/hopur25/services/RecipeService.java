package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();

    Recipe findRecipeById(Long id);

    Recipe addRecipe(Recipe recipe);

    void delete(Recipe recipe);

    Recipe findByTitle(String title);

    Recipe save(Recipe recipe);

    List<Recipe> searchByKeywordAndCriteria(String keyword, List<DietaryRestriction> selectedDietaryRestrictions, List<MealCategory> selectedMealCategories);
}
