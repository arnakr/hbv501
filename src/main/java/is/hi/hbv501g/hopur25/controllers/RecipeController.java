package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.S3Service;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;
    private final S3Service s3Service;

    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService, S3Service s3Service) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.s3Service = s3Service;
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

    /**
     * Displays a recipe edit form.
     *
     * @param recipeId ID of the recipe to be edited
     * @param model model to hold attributes for the view
     * @return the URL to update the page
     */
    @RequestMapping("/recipe/{recipeId}/edit-recipe")
    public String editRecipe(@PathVariable Long recipeId, Model model) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        if (recipe == null) {
            return "error";
        }
        model.addAttribute("recipe", recipe);
        return "/edit-recipe";
    }

    /**
     * Handles the submission of an edit-recipe form and updates recipe accordingly.
     *
     * @param updatedRecipe recipe object that contains the updated information
     * @param result binding result to validate the recipe data
     * @param model model to hold attributes for the view
     * @param session the HTTP session to retrieve logged-in user
     * @return redirect to the logged-in user's recipes
     */
    @RequestMapping(value = "/edit-recipe", method = RequestMethod.POST)
    public String updateRecipe(@ModelAttribute("recipe") Recipe updatedRecipe,
                               BindingResult result, Model model, HttpSession session) {
        Long recipeId = updatedRecipe.getRecipeId();
        Recipe currentRecipe = recipeService.findRecipeById(recipeId);

        currentRecipe.setTitle(updatedRecipe.getTitle());
        currentRecipe.setDescription(updatedRecipe.getDescription());
        currentRecipe.setCookTime(updatedRecipe.getCookTime());
        currentRecipe.setMealCategories(updatedRecipe.getMealCategories());
        currentRecipe.setDietaryRestrictions(updatedRecipe.getDietaryRestrictions());

        recipeService.updateRecipe(currentRecipe);
        return "redirect:/user-recipes";
    }

    /**
     * Displays the recipe create form.
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
     * @param recipeId The unique identifier of the recipe to be deleted
     * @param session The current HTTP session, used to retrieve the logged-in user
     * @return A redirect string that navigates the user back to their recipes page after deletion,
     *         or redirects to the login page if no user is logged in
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

    /**
     * Handles the file upload for a recipe's picture. This method allows the user to upload a new image
     * for their recipe, deletes the old image from the S3 bucket (if it exists), and updates the recipe
     * with the new image URL.
     *
     * @param recipeId The ID of the recipe whose picture is being updated
     * @param recipePicture The new image file uploaded by the user
     * @param session The HTTP session used to retrieve the logged-in user's details
     * @return A redirect to the "/user-recipes" page after successfully updating the recipe picture
     * @throws IOException If an error occurs during the file upload process to S3
     */
    @PostMapping("/recipe/{recipeId}/uploadRecipePicture")
    public String uploadRecipePicture(@PathVariable Long recipeId,
                                      @RequestParam("recipePicture") MultipartFile recipePicture,
                                      HttpSession session) throws IOException {
        System.out.println("Method triggered for recipe ID: " + recipeId);  // Add this log

        User currentUser = (User) session.getAttribute("LoggedInUser");
        if (currentUser == null) {
            return "redirect:/login";  // Ensure user is logged in
        }

        Recipe recipe = recipeService.findRecipeById(recipeId);

        System.out.println("Uploaded file: " + recipePicture.getOriginalFilename());
        System.out.println("File size: " + recipePicture.getSize() + " bytes");

        String oldPictureUrl = recipe.getRecipePictureUrl();
        if (oldPictureUrl != null) {
            String oldKey = oldPictureUrl.substring(oldPictureUrl.lastIndexOf("/") + 1);
            s3Service.deleteFile(oldKey);
        }

        String s3Url = s3Service.uploadFile(recipePicture);

        recipeService.updateRecipePicture(recipeId, s3Url);

        return "redirect:/user-recipes";
    }
}