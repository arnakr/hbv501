package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Recipe} entities.
 * This interface extends the {@link JpaRepository} interface,
 * providing methods for operations like create, read, update
 * and delete, and additional custom queries.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    /**
     * Saves a given {@link Recipe} entity.
     *
     * @param recipe the {@link Recipe} entity to be saved
     * @return the saved {@link Recipe} entity
     */
    Recipe save(Recipe recipe);

    /**
     * Deletes a given {@link Recipe} entity.
     *
     * @param recipe the {@link Recipe} entity to be deleted
     */
    void delete (Recipe recipe);

    /**
     * Retrieves all {@link Recipe} entities.
     *
     * @return a {@link List} of all {@link Recipe} entities
     */
    List<Recipe> findAll();

    /**
     * Finds {@link Recipe} entities by their title.
     *
     * @param title the title of the recipes to search for
     * @return a {@link List} of {@link Recipe} entities that match the specified title
     */
    List<Recipe> findByTitle(String title);

    Recipe findByRecipeId(Long id);

    List<Recipe> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description );

    List<Recipe> findByMealCategories(MealCategory mealCategory);

    @Query(
            value = "SELECT * FROM Recipes r WHERE r.title LIKE %?1%",
            nativeQuery = true)

    List<Recipe> search(String keyword);

    List<Recipe> findByDietaryRestrictions(DietaryRestriction dietaryRestriction);

    List<Recipe> findAllByOrderByUploadTimeAsc();

    List<Recipe> findAllByOrderByUploadTimeDesc();

    List<Recipe> findAllByOrderByTitleAsc();

    List<Recipe> findAllByOrderByTitleDesc();

    List<Recipe> findAllByOrderByCookTimeAsc();

    List<Recipe> findAllByOrderByCookTimeDesc();

    @Query("SELECT r.reviews FROM Recipe r WHERE r.id = :recipeId")
    List<Review> findReviewsByRecipeId(@Param("recipeId") Long recipeId);

    @Query("SELECT r FROM Recipe r JOIN r.reviews rev WHERE rev.user.id = :userId")
    List<Recipe> findRecipesReviewedByUserId(@Param("userId") Long userId);
}
