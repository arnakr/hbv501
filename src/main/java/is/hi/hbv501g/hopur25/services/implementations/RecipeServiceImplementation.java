package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository;
import is.hi.hbv501g.hopur25.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeServiceImplementation implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeServiceImplementation(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe findRecipeById(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        return optionalRecipe.orElse(null); // Return null if recipe not found, or throw an exception if preferred
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    public Recipe findByTitle(String title) {
        return recipeRepository.findByTitle(title).get(0);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public List<Recipe> searchByKeywordAndCriteria(String keyword, List<DietaryRestriction> selectedDietaryRestrictions, List<MealCategory> selectedMealCategories) {
        Set<Recipe> currentRecipes = new HashSet<>(recipeRepository.findAll());

        if (keyword != null) {
            List<Recipe> searchByKeyword = recipeRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
            currentRecipes.retainAll(searchByKeyword);
        }

        if (selectedDietaryRestrictions != null) {
            for (DietaryRestriction dietaryRestriction : selectedDietaryRestrictions) {
                List<Recipe> searchByDiet = recipeRepository.findByDietaryRestrictions(dietaryRestriction);
                currentRecipes.retainAll(searchByDiet);
            }
        }

        if (selectedMealCategories != null) {
            for (MealCategory mealCategory : selectedMealCategories) {
                List<Recipe> searchByMeal = recipeRepository.findByMealCategories(mealCategory);
                currentRecipes.retainAll(searchByMeal);
            }
        }

        return new ArrayList<>(currentRecipes);
    }
}
