package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Recipe save(Recipe recipe);
    void delete (Recipe recipe);

    List<Recipe> findAll();
    List<Recipe> findByTitle(String title);
    Optional<Recipe> findById(long id);
}
