package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupGET(User user) {
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPOST(User user, BindingResult result, Model model) {
        // Check for binding errors first
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid input. Please check your details.");
            return "signup"; // Redirect back to signup form
        }

        // Check if username already exists
        User exists = userService.findByUsername(user.getUsername());
        if (exists != null) {
            model.addAttribute("errorMessage", "Username already exists. Please choose another.");
            return "signup"; // Redirect back to signup form
        }

        // Set a default value for userPicture if it's not provided
        if (user.getUserPicture() == null || user.getUserPicture().isEmpty()) {
            user.setUserPicture("/images/default.jpg"); // Use a local placeholder
        }

        // Save the user
        userService.save(user);

        // Redirect to the home page after successful signup
        return "redirect:/";
    }

    // Login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(User user) {
        return "loginRequest";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "loginRequest";
        }
        User exists = userService.login(user);
        if (exists != null) {
            // Store user in session with a consistent key
            session.setAttribute("LoggedInUser", exists);
            model.addAttribute("LoggedInUser", exists);
            return "redirect:/";  // Redirect to home after login
        } else {
            // Add an error message to the model for invalid credentials
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
            return "loginRequest";  // Stay on the login page and show the error message
        }
    }

    // Logged in user page
    @RequestMapping(value = "/loggedin", method = RequestMethod.GET)
    public String loggedinGET(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            model.addAttribute("LoggedInUser", sessionUser);
            return "home";
        }
        return "redirect:/";
    }

    //show settings page
    @RequestMapping("/settings")
    public String settingsPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("LoggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("LoggedInUser", currentUser);
        return "settings";  // This will look for src/main/resources/templates/settings.html
    }

    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String settingsPost(@ModelAttribute("LoggedInUser") User updatedUser,
                               BindingResult result, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("LoggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

//        String confirmPassword = (String) session.getAttribute("confirmPassword");
//        if (!updatedUser.getPassword().equals(confirmPassword)) {
//            model.addAttribute("errorMessage", "Passwords do not match. Please try again.");
//            return "settings";
//        }

        String error = userService.updateUserSettings(currentUser, updatedUser);
        if (error != null) {
            model.addAttribute("errorMessage", error);
            return "settings";
        }

        session.setAttribute("LoggedInUser", updatedUser);
        model.addAttribute("LoggedInUser", updatedUser);

        return "home";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "home";
    }

    //show favorites page
    // Show the favorites page
    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public String showFavorites(HttpSession session, Model model) {
        // Get the logged-in user from session
        User currentUser = (User) session.getAttribute("LoggedInUser");
        if (currentUser == null) {
            return "redirect:/login";  // Redirect to login if not logged in
        }

        // Get the user's favorite recipes
        model.addAttribute("favoriteRecipes", userService.getUserFavorites(currentUser.getUserID()));
        model.addAttribute("LoggedInUser", currentUser);  // Add user info for the header or navigation
        return "favorites";  // Display the favorites.html template
    }
}
