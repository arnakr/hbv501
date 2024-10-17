package is.hi.hbv501g.hopur25.controllers;


import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable("id") long id, Model model) {
        Optional<Recipe> recipe = recipeService.findById(id);
        if (recipe.isPresent()) {
            model.addAttribute("recipe", recipe.get());
            return "recipe";
        } else {
            model.addAttribute("message", "Recipe not found");
            return "errorPage";  // Optional error page template
        }
    }
}

//import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
//import is.hi.hbv501g.hopur25.services.RecipeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;

//import java.util.Optional;

/*@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

   /**
    * @GetMapping("/{id}") public ResponseEntity<?> getRecipeById(@PathVariable("id") long id) {
    * Optional<Recipe> recipe = recipeService.findById(id);
    * if (recipe.isPresent()) {
    * return new ResponseEntity<>(recipe.get().toString(), HttpStatus.OK);
    * }
    * return new ResponseEntity<>("Uppskrift ekki til", HttpStatus.OK);
    * }

   @GetMapping("/recipe/{id}")
   public String getRecipeById(@PathVariable("id") long id) {
       Optional<Recipe> recipe = recipeService.findById(id);
       if (recipe.isPresent()) {
           return new ResponseEntity<>("Recipe ID: " + recipe.get().getRecipeId() + ", Title: " + recipe.get().getTitle(), HttpStatus.OK);
       } else {
           new ResponseEntity<>("Uppskrift ekki til", HttpStatus.NOT_FOUND);
       }
   }
}
*/
