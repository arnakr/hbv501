package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Recipe save(Recipe recipe);
    void delete (Recipe recipe);

    List<Recipe> findAll();
    List<Recipe> findByTitle(String title);
    Recipe findById(long recipeID);

   /* @Query("SELECT * FROM Recipe WHERE title LIKE %?1%")
    public List<Recipe> search(String keyword);
    */

    @Query(
            value = "SELECT * FROM Recipes r WHERE r.title LIKE %?1%",
            nativeQuery = true)
    List<Recipe> search(String keyword);
}
