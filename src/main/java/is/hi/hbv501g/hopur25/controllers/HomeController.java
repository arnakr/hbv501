package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.DietaryRestriction;
import is.hi.hbv501g.hopur25.persistence.entities.enumerations.MealCategory;
import is.hi.hbv501g.hopur25.services.RecipeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final RecipeService recipeService;

    @Autowired
    public HomeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Displays the home page with a list of recipes.
     * This method retrieves the current filters from the request parameters or session and applies them to the
     * recipe search. The results are sorted based on the selected sort order and displayed on the home page.
     *
     * @param sortOrder the sort order for the recipes (optional), can be "asc", "desc", "titillasc", "titilldesc", etc.
     * @param keyword the keyword for searching recipes (optional)
     * @param dietaryRestrictions the selected dietary restrictions for filtering recipes (optional)
     * @param mealCategories the selected meal categories for filtering recipes (optional)
     * @param model the Spring model object used to add attributes to the view
     * @param session the HTTP session used to persist filter selections across requests
     * @return the name of the view to be rendered (home page) with the filtered recipe list
     */
    @RequestMapping("/")
    public String getHomePage(
            @RequestParam(name = "sort", required = false) String sortOrder,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "dietaryRestrictions", required = false) List<DietaryRestriction> dietaryRestrictions,
            @RequestParam(name = "mealCategories", required = false) List<MealCategory> mealCategories,
            Model model, HttpSession session) {

        if (sortOrder == null) {
            sortOrder = (String) session.getAttribute("sortOrder");
        } else {
            session.setAttribute("sortOrder", sortOrder);
        }
        if (keyword == null) {
            keyword = (String) session.getAttribute("keyword");
        } else {
            session.setAttribute("keyword", keyword);
        }
        if (dietaryRestrictions == null) {
            dietaryRestrictions = (List<DietaryRestriction>) session.getAttribute("dietaryRestrictions");
        } else {
            session.setAttribute("dietaryRestrictions", dietaryRestrictions);
        }
        if (mealCategories == null) {
            mealCategories = (List<MealCategory>) session.getAttribute("mealCategories");
        } else {
            session.setAttribute("mealCategories", mealCategories);
        }

        List<Recipe> recipes = recipeService.searchByKeywordAndCriteria(keyword, dietaryRestrictions, mealCategories);

        for (Recipe recipe : recipes) {
            Double avgRating = recipe.getReviews().stream()
                    .filter(review->review.getRating() != null)
                    .mapToInt(review-> review.getRating())
                    .average()
                    .orElse(0.0);

            if (Double.isNaN(avgRating)) {
                avgRating = null;
            }
            recipe.setAvgRating(avgRating);
        }

        switch (sortOrder != null ? sortOrder.toLowerCase() : "") {
            case "asc":
                recipes.sort(Comparator.comparing(Recipe::getUploadTime));
                break;
            case "desc":
                recipes.sort(Comparator.comparing(Recipe::getUploadTime).reversed());
                break;
            case "titillasc":
                recipes.sort(Comparator.comparing(Recipe::getTitle));
                break;
            case "titilldesc":
                recipes.sort(Comparator.comparing(Recipe::getTitle).reversed());
                break;
            case "cooktimeasc":
                recipes.sort(Comparator.comparing(Recipe::getCookTime));
                break;
            case "cooktimedesc":
                recipes.sort(Comparator.comparing(Recipe::getCookTime).reversed());
                break;
            case "ratingasc":
                recipes.sort(Comparator.comparing(Recipe::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case "ratingdesc":
                recipes.sort(Comparator.comparing(Recipe::getAvgRating, Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            default:
                recipes.sort(Comparator.comparing(Recipe::getUploadTime).reversed());
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedDietaryRestrictions", dietaryRestrictions);
        model.addAttribute("selectedMealCategories", mealCategories);
        model.addAttribute("sortOrder", sortOrder);

        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        model.addAttribute("LoggedInUser", loggedInUser); // Add user to the model

        return "home";
    }

    /**
     * Clears the applied filters and redirects to the home page.
     * This method removes all filter-related session attributes (sort order, keyword, dietary restrictions,
     * and meal categories) and redirects the user to the home page without any filters applied.
     *
     * @param session the HTTP session from which the filter session attributes will be removed
     * @return a redirect URL to the home page with no filters applied
     */
    @RequestMapping("/clearFilters")
    public String clearFilters(HttpSession session) {
        session.removeAttribute("sortOrder");
        session.removeAttribute("keyword");
        session.removeAttribute("dietaryRestrictions");
        session.removeAttribute("mealCategories");
        return "redirect:/";
    }
}
