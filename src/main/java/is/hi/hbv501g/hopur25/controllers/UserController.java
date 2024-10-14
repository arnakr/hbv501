package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        return "signup"; // Return the signup page
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPOST(User user, BindingResult result, Model model, HttpSession session) {
        // Check for binding errors first
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Invalid input. Please check your details.");
            return "signup"; // Return to signup form with error
        }

        // Check if username already exists
        User exists = userService.findByUsername(user.getUsername());
        if (exists != null) {
            model.addAttribute("errorMessage", "Username already exists. Please choose another.");
            return "signup"; // Return to signup form with error
        }

        // Set a default value for userPicture if it's not provided
        if (user.getUserPicture() == null || user.getUserPicture().isEmpty()) {
            user.setUserPicture("/images/default.jpg"); // Use a local placeholder
        }

        // Save the user
        userService.save(user);

        // Store the user in session after successful signup
        session.setAttribute("LoggedInUser", user); // Store user in session

        // Redirect to the home page after successful signup
        return "redirect:/";
    }

    // Login
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGET(User user) {
        return "loginRequest"; // Return the login page
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginPOST(User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "loginRequest"; // Return to login form on error
        }
        User exists = userService.login(user);
        if (exists != null) {
            // Store user in session with a consistent key
            session.setAttribute("LoggedInUser", exists);
            return "redirect:/";  // Redirect to home after successful login
        }
        model.addAttribute("errorMessage", "Invalid credentials. Please try again.");
        return "loginRequest";  // Return to login if credentials are invalid
    }

    // Logged in user page
    @RequestMapping(value = "/loggedin", method = RequestMethod.GET)
    public String loggedinGET(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("LoggedInUser");
        if (sessionUser != null) {
            model.addAttribute("LoggedInUser", sessionUser);
            return "LoggedInUser";  // Return user-specific page
        }
        return "redirect:/";  // Redirect to home if not logged in
    }

    // Logout
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/"; // Redirect to home page after logout
    }
}
