package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService; // Change this to final and inject via constructor

    // Constructor injection for both RecipeService and UserService
    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable("id") Long id, Model model, HttpSession session) {
        Recipe recipe = recipeService.findRecipeById(id);
        if (recipe == null) {
            return "error";  // Show error page if the recipe is not found
        }

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("LoggedInUser", loggedInUser);
        }

        model.addAttribute("recipe", recipe);
        return "recipe";  // This renders the recipe detail page
    }

    @GetMapping("/user/{userId}/favorites")
    public String getFavoriteRecipes(@PathVariable Long userId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // If not logged in, redirect to login
        }

        model.addAttribute("LoggedInUser", loggedInUser);
        model.addAttribute("favoriteRecipes", userService.getUserFavorites(userId));
        return "favorites";
    }



    // Method to handle adding a recipe to favorites
    @PostMapping("/recipe/{recipeId}/addToFavorites")
    public String addFavoriteRecipe(@PathVariable Long recipeId, HttpSession session) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";  // If not logged in, redirect to login page
        }

        // Find the recipe by its ID
        Recipe recipe = recipeService.findRecipeById(recipeId);
        if (recipe == null) {
            return "error";  // Show error page if the recipe is not found
        }

        // Add the recipe to the user's favorites
        userService.addFavoriteRecipe(loggedInUser.getUserID(), recipe);

        // Redirect to the user's favorites page
        return "redirect:/favorites";
    }

    //Remove from favorites
    @PostMapping("/recipe/{recipeId}/removeFromFavorites")
    public String removeFavoriteRecipe(@PathVariable Long recipeId, HttpSession session) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // If not logged in, redirect to login page
        }

        // Find the recipe by ID
        Recipe recipe = recipeService.findRecipeById(recipeId);
        if (recipe == null) {
            return "error"; // Show error page if the recipe is not found
        }

        // Remove the recipe from the user's favorites
        userService.removeFavoriteRecipe(loggedInUser.getUserID(), recipe);

        // Redirect to the user's favorites page
        return "redirect:/user/" + loggedInUser.getUserID() + "/favorites";
    }




}
