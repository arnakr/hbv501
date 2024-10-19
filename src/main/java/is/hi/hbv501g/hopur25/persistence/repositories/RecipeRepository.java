package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Recipe save(Recipe recipe);
    void delete (Recipe recipe);

    List<Recipe> findAll();
    List<Recipe> findByTitle(String title);
    Recipe findByRecipeId(long recipeID);

    List<Recipe> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description );

    List<Recipe> findByMealCategories(MealCategory mealCategory);

    List<Recipe> findByDietaryRestrictions(DietaryRestriction dietaryRestriction);
}
