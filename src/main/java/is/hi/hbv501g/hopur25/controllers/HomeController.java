package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.entities.Review;
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
                recipes.sort(Comparator.comparing(Recipe::getAvgRating, Comparator.nullsLast(Comparator.naturalOrder())));
                break;
            case "ratingdesc":
                recipes.sort(Comparator.comparing(Recipe::getAvgRating, Comparator.nullsLast(Comparator.reverseOrder())));
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

    @RequestMapping("/clearFilters")
    public String clearFilters(HttpSession session) {
        session.removeAttribute("sortOrder");
        session.removeAttribute("keyword");
        session.removeAttribute("dietaryRestrictions");
        session.removeAttribute("mealCategories");
        return "redirect:/";
    }


}
