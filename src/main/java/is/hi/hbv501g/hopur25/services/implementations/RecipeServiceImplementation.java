package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository;
import is.hi.hbv501g.hopur25.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImplementation implements RecipeService {
    private final RecipeRepository recipeRepository;

    // private List<Recipe> recipeRepository = new ArrayList<>();
    //private int id_counter = 0;

    @Autowired
    public RecipeServiceImplementation(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    /*public Recipe findByID(long ID) {
        for (Recipe r: recipeRepository) {
            if (r.getRecipeID() == ID) {
                return r;
            }
        }
        return null;*/
    public Optional<Recipe> findByID(long id) {
        return recipeRepository.findById(id);
    }

    /*
    @Override
    public Recipe findByTitle(String title) {
        return recipeRepository.findByTitle(title).get(0); //skoða hérna hvað við viljum sækja
    }
    */

    @Override
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }

    public Recipe findByTitle(String title) {
        return recipeRepository.findByTitle(title).get(0);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}
