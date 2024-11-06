package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Controller
public class HomeController {
    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public HomeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    /*
    @RequestMapping("/")
    public String homePage(Model model, HttpSession session, @Param("keyword") String keyword) { // Ensure HttpSession is included
        List<Recipe> allRecipes = recipeService.searchByKeyword(keyword, keyword);
        model.addAttribute("recipes", allRecipes);
        model.addAttribute("keyword", keyword);

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        model.addAttribute("LoggedInUser", loggedInUser); // Add user to the model

        return "home"; // Return the home view
    }
     */

    /**
     * Displays the homePage with filters if chosen
     *
     * @param model                       the model to hold attributes for the view
     * @param session                     the HTTP session to retrieve the logged-in user
     * @param keyword                     an optional search keyword for filtering recipes
     * @param selectedDietaryRestrictions an optional list of dietary restrictions for filtering
     * @param selectedMealCategories      an optional list of meal categories for filtering
     * @return home page
     */
    @RequestMapping("/")
    public String homePage(Model model, HttpSession session,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "dietaryRestrictions", required = false) List<DietaryRestriction> selectedDietaryRestrictions,
                           @RequestParam(value = "mealCategories", required = false) List<MealCategory> selectedMealCategories) {
        // Filter recipes based on selected dietary restrictions, meal categories and keyword
        List<Recipe> filteredRecipes = recipeService.searchByKeywordAndCriteria(keyword, selectedDietaryRestrictions, selectedMealCategories);
        model.addAttribute("recipes", filteredRecipes);

        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedDietaryRestrictions", selectedDietaryRestrictions);
        model.addAttribute("selectedMealCategories", selectedMealCategories);

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        model.addAttribute("LoggedInUser", loggedInUser); // Add user to the model

        return "home"; // Return the home view
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
        if (result.hasErrors()) {
            return "createRecipe";  // Return to the form if there are errors
        }

        // Retrieve the logged-in user from the session
        User currentUser = (User) session.getAttribute("LoggedInUser");

        if (currentUser != null) {
            if (currentUser.getUserRecipes() == null) {
                currentUser.setUserRecipes(new ArrayList<>());
            }
            currentUser.getUserRecipes().add(recipe);
            recipe.setUser(currentUser);
        }

        // Set the default recipe picture if none is provided
        if (recipe.getRecipePictureUrl() == null || recipe.getRecipePictureUrl().isEmpty()) {
            recipe.setRecipePictureUrl("/images/food.png");
        }


        recipeService.save(recipe);
        return "redirect:/user/recipes";
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
            return "redirect:/user/recipes";
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


        return "redirect:/user/recipes";
    }
}
