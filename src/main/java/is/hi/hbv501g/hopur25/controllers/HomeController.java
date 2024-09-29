
package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;

import is.hi.hbv501g.hopur25.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        model.addAttribute("recipes", recipeService.findAll());
        model.addAttribute("users", userService.findAll());
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(User user, Model model) {

        return "loginRequest";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(User user, BindingResult result, Model model) {
        if (result.hasErrors()){
            return "loginRequest";
        }
        return "redirect:/";
    }

}
