package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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



   /* @Query("SELECT * FROM Recipe WHERE title LIKE %?1%")
    public List<Recipe> search(String keyword);
    */

    @Query(
            value = "SELECT * FROM Recipes r WHERE r.title LIKE %?1%",
            nativeQuery = true)
    List<Recipe> search(String keyword);

    List<Recipe> findByDietaryRestrictions(DietaryRestriction dietaryRestriction);

    List<Recipe> findAllByOrderByUploadTimeAsc();

    List<Recipe> findAllByOrderByUploadTimeDesc();

}
