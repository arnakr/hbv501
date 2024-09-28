
package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.services.RecipeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    private RecipeService recipeService;

    @Autowired
    public HomeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // This method handles HTTP GET requests to "/"
    @RequestMapping ("/")
    public String homePage(Model model) {
        model.addAttribute("recipes", recipeService.findAll());

        return "home.html";
    }
}
