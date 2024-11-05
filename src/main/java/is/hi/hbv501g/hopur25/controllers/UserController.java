package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.User;
// import is.hi.hbv501g.hopur25.services.S3Service;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {
    private final UserService userService;
    // private final S3Service s3Service;

    /*
    @Autowired
    public UserController(UserService userService, S3Service s3service) {
        this.userService = userService;
        this.s3Service = s3service;
    }
     */

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the GET request for the signup page.
     *
     * @param user The user object (optional).
     * @return The name of the signup view to render.
     */
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupGET(User user) {
        return "signup";
    }

    /**
     * Handles user signup submissions.
     * <p>
     * Validates input and checks for existing usernames and emails.
     * Saves the new user if all checks pass; otherwise,
     * returns an error message in the form.
     *
     * @param user   The user object containing signup data.
     * @param result Holds validation errors, if any.
     * @param model  The model for passing attributes to the view.
     * @return Redirects to the home page if signup is successful, otherwise
     * returns to the signup view on error.
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPOST(User user, BindingResult result, Model model) {
        // Check for binding errors first
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Ógild inntak. Vinsamlegast athugaðu upplýsingarnar þínar");
            return "signup"; // Redirect back to signup form
        }

        // Check if username already exists
        User existsByUsername = userService.findByUsername(user.getUsername());
        if (existsByUsername != null) {
            model.addAttribute("errorMessage", "Notendanafn er þegar til. Vinsamlegast veldu annað");
            return "signup"; // Redirect back to signup form
        }

        // Check if email already exists
        User existsByEmail = userService.findByEmail(user.getEmail());
        if (existsByEmail != null) {
            model.addAttribute("errorMessage", "Netfang er þegar skráð. Vinsamlegast notaðu annað netfang");
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

    /**
     * Handles the GET request for the login page.
     *
     * @param user The user object (optional).
     * @return The name of the login view to render.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(User user) {
        return "loginRequest";
    }

    /**
     * Handles user login submissions.
     * <p>
     * Validates input and authenticates the user. If successful login,
     * stores the user in the session and redirects to the home page.
     * If authentication fails, returns to the login page with an error message.
     *
     * @param user    The user object containing login credentials.
     * @param result  Holds validation errors, if any.
     * @param model   The model for passing attributes to the view.
     * @param session The HTTP session to store logged-in user data.
     * @return Redirects to the home page if login is successful,
     * or returns to the login page on error.
     */
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
            model.addAttribute("errorMessage", "Rangt notendanafn eða lykilorð. Vinsamlega reyndu aftur");
            return "loginRequest";  // Stay on the login page and show the error message
        }
    }


    /**
     * Displays the settings page for the logged-in user.
     * <p>
     * Redirects to the login page if the user is not authenticated.
     *
     * @param session The HTTP session to retrieve the logged-in user.
     * @param model   The model for passing attributes to the view.
     * @return The name of the settings view or
     * if not logged in, redirect to the login page.
     */
    @RequestMapping("/settings")
    public String settingsPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("LoggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("LoggedInUser", currentUser);
        return "settings";  // This will look for src/main/resources/templates/settings.html
    }

    /**
     * Processes the settings update submission.
     * <p>
     * Validates the user and updates settings. Redirects to the login page
     * if the user is not authenticated. Returns error messages if any updates fail.
     *
     * @param updatedUser The user object containing updated settings.
     * @param result      Holds validation errors, if any.
     * @param model       The model for passing attributes to the view.
     * @param session     The HTTP session to retrieve the logged-in user.
     * @return Redirects to the home page if settings are updated successfully,
     * or returns to the settings page on error.
     */
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

    /**
     * Handles user logout by invalidating the current session.
     * <p>
     * Redirects the user to the home page after logging out.
     *
     * @param session The HTTP session to be invalidated.
     * @return A redirect to the home page.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /**
     * Displays the favorites page for the logged-in user.
     * <p>
     * Redirects to the login page if the user is not authenticated.
     * Retrieves the user's favorite recipes and adds them to the model
     * for display on the favorites page.
     *
     * @param session The HTTP session to retrieve the logged-in user.
     * @param model   The model for passing attributes to the view.
     * @return The name of the favorites view or a redirect to the login page.
     */
    @RequestMapping(value = "/favorites", method = RequestMethod.GET)
    public String showFavorites(HttpSession session, Model model) {
        // Get the logged-in user from session
        User currentUser = (User) session.getAttribute("LoggedInUser");
        if (currentUser == null) {
            return "redirect:/login";  // Redirect to login if not logged in
        }

        // Get the user's favorite recipes
        model.addAttribute("favoriteRecipes", userService.getUserFavorites(currentUser.getUserID()));
        model.addAttribute("LoggedInUser", currentUser);  // Add user info for the header
        return "favorites";  // Display the favorites.html template
    }

    /**
     * Displays the user's recipes page for a logged-in user.
     * Redirects to the login page if the user is not authenticated.
     *
     * @param session The HTTP session to retrieve the logged-in user.
     * @param model   The model for passing attributes to the view.
     * @return String of name of the user-recipes view or redirect to login page.
     */
    @RequestMapping(value = "/user-recipes", method = RequestMethod.GET)
    public String showUserRecipes(HttpSession session, Model model) {
        // Check if user is logged in
        User currentUser = (User) session.getAttribute("LoggedInUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Get the user's posted recipes
        model.addAttribute("userRecipes", userService.getUserRecipes(currentUser.getUserID()));
        model.addAttribute("LoggedInUser", currentUser);
        return "user-recipes";
    }

    /**
     * Deletes the currently logged-in user's account and redirects to the home page.
     *
     * @param session the HTTP session to retrieve the logged-in user
     * @return a redirect to the home page, or the login page if the user is not authenticated
     */
    @PostMapping("/deleteUser")
    public String deleteUser(HttpSession session) {
        // Retrieve the logged-in user from the session
        User currentUser = (User) session.getAttribute("LoggedInUser");

        // Check if the user is logged in
        if (currentUser == null) {
            return "redirect:/login"; // Redirect to log in if not authenticated
        }

        // Delete the user using the user's ID
        userService.deleteUser(currentUser.getUserID());

        // Invalidate the session after deletion
        session.invalidate();

        // Redirect to the home page after deletion
        return "redirect:/";
    }

    /**
     * Uploads profile picture for the user.
     *
     * This method checks if the user is logged in, if not redirect to login page
     * deletes the existing profile picture from S3 (if it exists),
     * generates a unique filename for the new uploaded image,
     * uploads it to Amazon S3, and updates the user's profile picture URL in the database.
     *
     * @param profilePicture the MultipartFile containing the new profile picture
     * @param session the current HTTP session
     * @return a redirect string to the settings page or login page if the user is not logged in
     * @throws IOException if an error occurs during file operations or uploading to S3
     */
    /*
    @PostMapping("/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture, HttpSession session) throws IOException {
        User currentUser = (User) session.getAttribute("LoggedInUser");

        // Check if the user is logged in, if not redirect to login page
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Delete the old profile picture if it exists
        String oldPictureUrl = currentUser.getUserPicture();
        if (oldPictureUrl != null) {
            // Extract the key from the old picture URL
            String oldKey = oldPictureUrl.substring(oldPictureUrl.lastIndexOf("/") + 1); // Get the key from the URL
            s3Service.deleteFile(oldKey); // Delete the old file from S3
        }

        // Upload the new image to S3 using the instance of S3Service
        String s3Url = s3Service.uploadFile(profilePicture);

        // Update the user profile picture in the database
        userService.updateUserProfilePicture(currentUser.getUserID(), s3Url);

        // Update the session with the new picture URL
        currentUser.setUserPicture(s3Url);
        session.setAttribute("LoggedInUser", currentUser);

        return "redirect:/settings"; // Redirect to settings
    }

     */



}
