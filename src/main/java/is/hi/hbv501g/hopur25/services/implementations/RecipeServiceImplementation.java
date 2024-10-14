package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository;
import is.hi.hbv501g.hopur25.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceImplementation implements RecipeService {
    private RecipeRepository recipeRepository;

   // private List<Recipe> recipeRepository = new ArrayList<>();
    //private int id_counter = 0;

    @Autowired
    public RecipeServiceImplementation(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
       // recipeRepository.add(new Recipe("title 1", new ArrayList<>(), 1 , "description 1"));
        //recipeRepository.add(new Recipe("title 2", new ArrayList<>(), 2 , "description 2"));
        //recipeRepository.add(new Recipe("title 3", new ArrayList<>(), 3 , "description 3"));
        //for (Recipe r: recipeRepository) {
          //  r.setRecipeID(id_counter);
            //id_counter++;
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
    public Recipe findByID(long ID) {
        return recipeRepository.findById(ID);
    }

    @Override
    public Recipe findByTitle(String title) {
        return recipeRepository.findByTitle(title).getFirst(); //skoða hérna hvað við viljum sækja
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        /*recipe.setRecipeID(id_counter);
        id_counter++;
        recipeRepository.add(recipe);
        return recipe;*/
        return recipeRepository.save(recipe);
    }

    @Override
    public void delete(Recipe recipe) {
        recipeRepository.delete(recipe);
    }
}
