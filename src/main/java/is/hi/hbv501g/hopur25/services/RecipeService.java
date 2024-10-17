package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    Recipe findById(long id);
    Recipe addRecipe(Recipe recipe);
    void delete(Recipe recipe);
    Recipe findByTitle(String title);

    Recipe save(Recipe recipe);

    List<Recipe> searchByKeyword(String keyword);
}
