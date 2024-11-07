package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    /**
     * Retrieves a recipe by its ID and adds it to the model for display.
     * If the recipe is not found, returns an error page.
     *
     * @param id      the ID of the recipe to retrieve
     * @param model   the model to hold attributes for the view
     * @param session the HTTP session to retrieve the logged-in user
     * @return the name of the view template for the recipe detail page or error
     */
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

    /**
     * Retrieves the favorite recipes of a user by their user ID.
     * Redirects to the login page if the user is not logged in.
     *
     * @param userId  the ID of the user whose favorites to retrieve
     * @param model   the model to hold attributes for the view
     * @param session the HTTP session to retrieve the logged-in user
     * @return the name of the view template for the favorites page
     */
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

    /**
     * Adds a recipe to the logged-in user's favorites.
     * Redirects to the login page if the user is not logged in.
     *
     * @param recipeId the ID of the recipe to add to favorites
     * @param session  the HTTP session to retrieve the logged-in user
     * @return the redirect URL to the user's favorites page
     */
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

    /**
     * Removes a recipe from the logged-in user's favorites.
     * Redirects to the login page if the user is not logged in.
     *
     * @param recipeId the ID of the recipe to remove from favorites
     * @param session  the HTTP session to retrieve the logged-in user
     * @return the redirect URL to the user's favorites page
     */
    @PostMapping("/recipe/{recipeId}/removeFromFavorites")
    public String removeFavoriteRecipe(@PathVariable Long recipeId, HttpSession session) {
        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Recipe recipe = recipeService.findRecipeById(recipeId);
        if (recipe == null) {
            return "error";
        }
        userService.removeFavoriteRecipe(loggedInUser.getUserID(), recipe);
        return "redirect:/user/" + loggedInUser.getUserID() + "/favorites";
    }

    @RequestMapping("/recipe/{recipeId}/edit-recipe")
    public String editRecipe(@PathVariable Long recipeId, Model model) {
        Recipe recipe = recipeService.findRecipeById(recipeId);

        // Check if recipe exists
        if (recipe == null) {
            return "error"; // Handle recipe not found error
        }

        System.out.println("OLD RECIPE:" + recipe.toString());
        model.addAttribute("recipe", recipe);
        return "/edit-recipe"; // Update template path (assuming it's different)
    }

    @RequestMapping(value = "/edit-recipe", method = RequestMethod.POST)
    public String updateRecipe(@ModelAttribute("recipe") Recipe updatedRecipe,
                               BindingResult result, Model model, HttpSession session) {
        Long recipeId = updatedRecipe.getRecipeId();
        Recipe currentRecipe = recipeService.findRecipeById(recipeId);

        System.out.println("NEW INFORMATION:" + updatedRecipe.toString());
        System.out.println("RECIPE ID: " + recipeId);

        // Update specific fields of the original recipe
        currentRecipe.setTitle(updatedRecipe.getTitle());
        currentRecipe.setDescription(updatedRecipe.getDescription());
        currentRecipe.setCookTime(updatedRecipe.getCookTime());
        currentRecipe.setMealCategories(updatedRecipe.getMealCategories()); // Assuming you want to overwrite meal categories entirely
        currentRecipe.setDietaryRestrictions(updatedRecipe.getDietaryRestrictions()); // Assuming you want to overwrite dietary restrictions entirely

        System.out.println("NEW RECIPE: " + currentRecipe.toString());
        // Update the DB
        recipeService.updateRecipe(currentRecipe);

        return "redirect:/user-recipes"; // Redirect user to updated recipe page
    }

    /**
     * Displays the recipe create form
     *
     * @param model the model to hold attributes for the view
     * @return /createRecipe
     */
    @RequestMapping(value = "/createRecipe", method = RequestMethod.GET)
    public String createRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "createRecipe";  // points to createRecipe.html
    }

    /**
     * Handles the submission of the recipe creation form.
     *
     * @param recipe  the Recipe object populated with the form data
     * @param result  the binding result for validating the recipe data
     * @param session the HTTP session to retrieve the logged-in user
     * @return a redirect to the home page or the recipe creation form if there are errors
     */
    @RequestMapping(value = "/createRecipe", method = RequestMethod.POST)
    public String createRecipeSubmit(@ModelAttribute Recipe recipe, BindingResult result, HttpSession session) {
        // Check if the user is logged in
        User currentUser = (User) session.getAttribute("LoggedInUser");

        // If the user is not logged in, redirect to the login page
        if (currentUser == null) {
            return "redirect:/login";  // Redirect to the login page if the user is not logged in
        }

        // If the form has errors, return to the form
        if (result.hasErrors()) {
            return "createRecipe";  // Return to the form if there are errors
        }

        // Proceed with saving the recipe for the logged-in user
        if (currentUser.getUserRecipes() == null) {
            currentUser.setUserRecipes(new ArrayList<>());
        }
        currentUser.getUserRecipes().add(recipe);
        recipe.setUser(currentUser);

        // Set the default recipe picture
        if (recipe.getRecipePictureUrl() == null || recipe.getRecipePictureUrl().isEmpty()) {
            recipe.setRecipePictureUrl("/images/food.png");
        }

        // Save the recipe
        recipeService.save(recipe);

        // Redirect to the user recipes page after successful creation
        return "redirect:/user-recipes";
    }


    /**
     * Handles the deletion of a recipe by its ID.
     * If no user is logged in, the method redirects to the login page.
     * If the recipe is found, it proceeds with deletion.
     *
     * @param recipeId The unique identifier of the recipe to be deleted.
     * @param session The current HTTP session, used to retrieve the logged-in user.
     * @return A redirect string that navigates the user back to their recipes page after deletion,
     *         or redirects to the login page if no user is logged in.
     */
    @RequestMapping(value = "/recipe/{recipeId}/deleteRecipe", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable Long recipeId, HttpSession session) {
        User currentUser = (User) session.getAttribute("LoggedInUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        Recipe recipe = recipeService.findRecipeById(recipeId);

        if (recipe == null) {
            return "redirect:/user-recipes";
        }

//        System.out.println("Current user ID: " + currentUser.getUserID());
//        System.out.println("Recipe owner ID: " + (recipe.getUser() != null ? recipe.getUser().getUserID() : "null"));

//        if (recipe.getUser() != null && recipe.getUser().equals(currentUser)) {
//            recipeService.delete(recipeId);  // Delete the recipe
//            System.out.println("Recipe deleted successfully.");
//        } else {
//            System.out.println("User does not own this recipe or recipe owner is null.");
//        }

        recipeService.delete(recipeId, currentUser);


        return "redirect:/user-recipes";
    }
}