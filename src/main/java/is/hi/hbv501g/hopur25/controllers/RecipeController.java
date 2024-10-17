package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import is.hi.hbv501g.hopur25.services.RecipeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable("id") Long id, Model model) {
        Recipe recipe = recipeService.findRecipeById(id);
        if (recipe == null) {
            return "error";
        }
        model.addAttribute("recipe", recipe);
        return "recipe";
    }


    @GetMapping("/user/{userId}/favorites")
    public String getFavoriteRecipes(@PathVariable Long userId, Model model) {
        model.addAttribute("favorites", userService.getUserFavorites(userId));
        return "favorites";
    }


    @PostMapping("/user/{userId}/favorites/{recipeId}")
    public String addFavoriteRecipe(@PathVariable Long userId, @PathVariable Long recipeId) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        userService.addFavoriteRecipe(userId, recipe);
        return "redirect:/user/" + userId + "/favorites";
    }
}