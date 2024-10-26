package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository;
import is.hi.hbv501g.hopur25.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the {@link RecipeService} interface.
 * This class provides the business logic for managing {@link Recipe} entities,
 * utilizing the {@link RecipeRepository} for data access.
 */
@Service
public class RecipeServiceImplementation implements RecipeService {
    private final RecipeRepository recipeRepository;

    /**
     * Constructor to initialize the RecipeRepository.
     *
     * @param recipeRepository the {@link RecipeRepository} to be used for data access
     */
    @Autowired
    public RecipeServiceImplementation(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Retrieves all recipes from the repository.
     *
     * @return a {@link List} of all {@link Recipe} objects
     */
    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    /**
     * Retrieves a recipe by its unique identifier.
     *
     * @param id the unique identifier of the recipe
     * @return the {@link Recipe} object with the specified ID, or {@code null} if not found
     */
    @Override
    public Recipe findRecipeById(Long id) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        return optionalRecipe.orElse(null); // Return null if recipe not found, or throw an exception if preferred
    }

    /**
     * Adds a new recipe to the repository.
     *
     * @param recipe the {@link Recipe} object to add
     * @return the added {@link Recipe} object
     */
    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    /**
     * Deletes the specified recipe from the repository.
     *
     * @param recipe the {@link Recipe} object to delete
     */
    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    //Skoða hvort við ætlum að hafa þetta, findByTitle
    /**
     * Finds a recipe by its title.
     *
     * @param title the title of the recipe to search for
     * @return the {@link Recipe} object with the specified title, or {@code null} if not found
     * @throws IndexOutOfBoundsException if no recipe is found with the specified title
     */
    public Recipe findByTitle(String title) {
        return recipeRepository.findByTitle(title).get(0);
    }

    /**
     * Saves a recipe to the repository.
     * This method can be used to update an existing recipe or add a new one.
     *
     * @param recipe the {@link Recipe} object to save
     * @return the saved {@link Recipe} object
     */
    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    /**
     * Searches for recipes that contain the specified keyword.
     * If the keyword is {@code null}, retrieves all recipes.
     *
     * @param keyword the keyword to search for
     * @return a {@link List} of {@link Recipe} objects that match the search criteria,
     *         or all recipes if the keyword is {@code null}
     */
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
//setja javadocs
   @Override
    public List<Recipe> getRecipesSortedByUploadTimeAsc() {
        return recipeRepository.findAllByOrderByUploadTimeAsc();
}
    @Override
    public List<Recipe> getRecipesSortedByUploadTimeDesc() {
        return recipeRepository.findAllByOrderByUploadTimeDesc();
    }
}

