package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {
    private final RecipeService recipeService;
    private final UserService userService;

    @Autowired
    public HomeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @RequestMapping("/")
    public String homePage(Model model, HttpSession session, @Param("keyword") String keyword) { // Ensure HttpSession is included
        List<Recipe> allRecipes = recipeService.searchByKeyword(keyword);
        model.addAttribute("recipes", allRecipes);
        model.addAttribute("keyword", keyword);

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        model.addAttribute("LoggedInUser", loggedInUser); // Add user to the model

        return "home"; // Return the home view
    }

    @RequestMapping(value = "/createRecipe", method = RequestMethod.GET)
    public String createRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "createRecipe";  // points to createRecipe.html
    }

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
            return "redirect:/login";  // Redirect to login if no user is logged in
        }

        recipeService.save(recipe);  // Save the recipe
        return "redirect:/";  // Redirect to home after saving
    }
}
