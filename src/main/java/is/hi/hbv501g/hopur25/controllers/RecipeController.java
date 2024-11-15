package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.ReviewService;
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
    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService, ReviewService reviewService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.reviewService = reviewService;
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
            return "error";
        }

        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("LoggedInUser", loggedInUser);
        }
        model.addAttribute("recipe", recipe);
        return "recipe";
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
            return "redirect:/login";
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

        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Recipe recipe = recipeService.findRecipeById(recipeId);
        if (recipe == null) {
            return "error";
        }
        userService.addFavoriteRecipe(loggedInUser.getUserID(), recipe);
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
        if (recipe == null) {
            return "error";
        }
        System.out.println("OLD RECIPE:" + recipe.toString());
        model.addAttribute("recipe", recipe);
        return "/edit-recipe";
    }

    @RequestMapping(value = "/edit-recipe", method = RequestMethod.POST)
    public String updateRecipe(@ModelAttribute("recipe") Recipe updatedRecipe,
                               BindingResult result, Model model, HttpSession session) {
        Long recipeId = updatedRecipe.getRecipeId();
        Recipe currentRecipe = recipeService.findRecipeById(recipeId);

        System.out.println("NEW INFORMATION:" + updatedRecipe.toString());
        System.out.println("RECIPE ID: " + recipeId);

        currentRecipe.setTitle(updatedRecipe.getTitle());
        currentRecipe.setDescription(updatedRecipe.getDescription());
        currentRecipe.setCookTime(updatedRecipe.getCookTime());
        currentRecipe.setMealCategories(updatedRecipe.getMealCategories());
        currentRecipe.setDietaryRestrictions(updatedRecipe.getDietaryRestrictions());

        System.out.println("NEW RECIPE: " + currentRecipe.toString());
        recipeService.updateRecipe(currentRecipe);
        return "redirect:/user-recipes";
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
        return "createRecipe";
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
        User currentUser = (User) session.getAttribute("LoggedInUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "createRecipe";
        }

        if (currentUser.getUserRecipes() == null) {
            currentUser.setUserRecipes(new ArrayList<>());
        }
        currentUser.getUserRecipes().add(recipe);
        recipe.setUser(currentUser);

        if (recipe.getRecipePictureUrl() == null || recipe.getRecipePictureUrl().isEmpty()) {
            recipe.setRecipePictureUrl("/images/food.png");
        }

        recipeService.save(recipe);
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
        recipeService.delete(recipeId, currentUser);
        return "redirect:/user-recipes";
    }

  /*  @PostMapping("/recipe/{recipeId}/addReview")
    public String addReview(@PathVariable Long recipeId, @RequestParam String comment, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Review review = new Review();
        review.setComment(comment);
        review.setRecipe(recipeService.findRecipeById(recipeId));
        review.setUser(loggedInUser);

        reviewService.saveReview(review);

        return "redirect:/recipe/" + recipeId;
    } */
}