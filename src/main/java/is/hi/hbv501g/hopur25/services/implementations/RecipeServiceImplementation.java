package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository;
import is.hi.hbv501g.hopur25.persistence.repositories.ReviewRepository;
import is.hi.hbv501g.hopur25.persistence.repositories.UserRepository;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link RecipeService} interface.
 * This class provides the business logic for managing {@link Recipe} entities,
 * utilizing the {@link RecipeRepository} for data access.
 */
@Service
public class RecipeServiceImplementation implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Constructor to initialize the RecipeRepository.
     *
     * @param recipeRepository the {@link RecipeRepository} to be used for data access
     */
    @Autowired
    public RecipeServiceImplementation(RecipeRepository recipeRepository, ReviewRepository reviewRepository, UserRepository userRepository, @Qualifier("userService") UserService userService) {
        this.recipeRepository = recipeRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    /**
     * Retrieves a recipe by its unique identifier.
     *
     * @param id the unique identifier of the recipe
     * @return the {@link Recipe} object with the specified ID, or {@code null} if not found
     */
    @Override
    public Recipe findRecipeById(Long id) {
        return recipeRepository.findByRecipeId(id);
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
     * Sorts a list of recipes based on the specified sort order.
     * The method supports sorting by upload time, title, and cook time in both ascending and descending order.
     *
     * @param recipes   the list of recipes to be sorted
     * @param sortOrder the sort order, which can be one of the following:
     *                  "asc" for ascending upload time,
     *                  "desc" for descending upload time,
     *                  "titillasc" for ascending title,
     *                  "titilldesc" for descending title,
     *                  "cooktimeasc" for ascending cook time,
     *                  "cooktimedesc" for descending cook time,
     *                  "ratingasc" for ascending average rating,
     *                  "ratingdesc" for descending average rating
     * @return a sorted list of recipes based on the specified sort order
     */
    private List<Recipe> sortRecipes(List<Recipe> recipes, String sortOrder) {
        return switch (sortOrder.toLowerCase()) {
            case "asc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getUploadTime))
                    .collect(Collectors.toList());
            case "desc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getUploadTime).reversed())
                    .collect(Collectors.toList());
            case "titillasc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getTitle))
                    .collect(Collectors.toList());
            case "titilldesc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getTitle).reversed())
                    .collect(Collectors.toList());
            case "cooktimeasc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getCookTime))
                    .collect(Collectors.toList());
            case "cooktimedesc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getCookTime).reversed())
                    .collect(Collectors.toList());
            case "ratingasc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
            case "ratingdesc" -> recipes.stream()
                    .sorted(Comparator.comparing(Recipe::getAvgRating, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            default -> recipes; // No sorting if sortOrder is invalid
        };
    }

    @Override
    public void updateRecipe (Recipe updatedRecipe) {
        recipeRepository.save(updatedRecipe);
    }

    @Override
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReview(int id) {
        return reviewRepository.findById((long) id).orElse(null);
    }

    @Override
    public void deleteReview(int id) {
        reviewRepository.deleteById((long) id);
    }

    /**
     * Updates the recipe's picture URL in the database with the given S3 URL.
     * This method retrieves the recipe by its ID and sets the new recipe picture URL.
     *
     * <p>After updating the recipe's picture URL, the recipe is saved back to the database.</p>
     *
     * @param recipeId The ID of the recipe whose picture URL is to be updated.
     * @param s3Url The new URL of the recipe picture stored in S3.
     * @throws RuntimeException If the recipe with the provided ID is not found.
     */
    public void updateRecipePicture(Long recipeId, String s3Url) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.setRecipePictureUrl(s3Url);  // Update the picture URL
        recipeRepository.save(recipe);  // Save the recipe with the updated picture
    }
}

