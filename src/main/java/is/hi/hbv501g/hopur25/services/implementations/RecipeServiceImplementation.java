package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository;
import is.hi.hbv501g.hopur25.persistence.repositories.UserRepository;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Constructor to initialize the RecipeRepository.
     *
     * @param recipeRepository the {@link RecipeRepository} to be used for data access
     */
    @Autowired
    public RecipeServiceImplementation(RecipeRepository recipeRepository, UserRepository userRepository, @Qualifier("userService") UserService userService) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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
        return recipeRepository.findByRecipeId(id); // Return null if recipe not found, or throw an exception if preferred
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
     * @param recipeId the {@link Recipe} object to delete
     */
    @Transactional
    @Override
    public void delete(Long recipeId, User currentUser) {
        Recipe recipe = recipeRepository.findById(recipeId).orElse(null);
        currentUser.getUserRecipes().remove(recipe);
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

    /**
     * Retrieves a list of recipes sorted in ascending order by their upload time.
     *
     * @return a List of {@link Recipe} objects sorted from the oldest to the newest based on the upload time.
     */
   @Override
    public List<Recipe> getRecipesSortedByUploadTimeAsc() {
        return recipeRepository.findAllByOrderByUploadTimeAsc();
    }

    /**
     * Retrieves a list of recipes sorted in descending order by their upload time.
     *
     * @return a List of {@link Recipe} objects sorted from the newest to the oldest based on the upload time.
     */
    @Override
    public List<Recipe> getRecipesSortedByUploadTimeDesc() {
        return recipeRepository.findAllByOrderByUploadTimeDesc();
    }

    /**
     * Retrieves a list of recipes sorted in ascending alphabetical order
     *
     * @return a List of {@link Recipe} objects sorted in ascending alphabetical order
     */
    @Override
    public List<Recipe> getRecipesSortedByTitleAsc() {
        return recipeRepository.findAllByOrderByTitleAsc();
    }

    /**
     * Retrieves a list of recipes sorted in descending alphabetical order
     *
     * @return a List of {@link Recipe} objects sorted in descending alphabetical order
     */
    @Override
    public List<Recipe> getRecipesSortedByTitleDesc() {
        return recipeRepository.findAllByOrderByTitleDesc();
    }

    @Override
    public List<Recipe> getRecipeSortedByCooktimeAsc() {return recipeRepository.findAllByOrderByCookTimeAsc(); }

    @Override
    public List<Recipe> getRecipeSortedByCooktimeDesc() { return recipeRepository.findAllByOrderByCookTimeDesc(); }


    @Override
    public void updateRecipe (Recipe updatedRecipe) {
        recipeRepository.save(updatedRecipe);
    }
}

