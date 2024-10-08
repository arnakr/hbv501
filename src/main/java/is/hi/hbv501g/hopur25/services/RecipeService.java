package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    Recipe findByID(long ID);
    Recipe addRecipe(Recipe recipe);
    void deleteRecipe(Recipe recipe);
}
