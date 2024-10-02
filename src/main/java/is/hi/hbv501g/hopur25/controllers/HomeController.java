
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {
    private RecipeService recipeService;
    private UserService userService;

    @Autowired
    public HomeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    // This method handles HTTP GET requests to "/"
    @RequestMapping ("/")
    public String homePage(Model model) {

        List<Recipe> allRecipes = recipeService.findAll();

        model.addAttribute("recipes", allRecipes);

       // model.addAttribute("recipes", recipeService.findAll());
       // model.addAttribute("users", userService.findAll());
        return "home";
    }
}
