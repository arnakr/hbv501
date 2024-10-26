package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
     * Displays the the homePage with filters if chosen
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
            recipe.setUser(currentUser);  // Set the user for the recipe
        } else {
            // Optionally, handle the case where there is no logged-in user
            return "redirect:/login";  // Redirect to log in if no user is logged in
        }

        recipeService.save(recipe);  // Save the recipe
        return "redirect:/";  // Redirect to home after saving
    }
}
