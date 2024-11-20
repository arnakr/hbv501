package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class ReviewController {
    private final RecipeService recipeService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(RecipeService recipeService, ReviewService reviewService) {
        this.recipeService = recipeService;
        this.reviewService = reviewService;
    }

    /**
     * Adds a review to a recipe.
     * This method allows a logged-in user to add a review to a specific recipe, including a comment and a rating.
     * The method validates that the user is logged in and that the rating is provided.
     *
     * @param recipeId the ID of the recipe to which the review is being added
     * @param comment  the content of the review's comment
     * @param rating   the rating given by the user
     * @param session  the HTTP session that contains the logged-in user
     * @return a redirect URL to the recipe page or a login page if the user is not logged in
     * @throws IllegalArgumentException if the rating is not provided
     */
    @PostMapping("/recipe/{recipeId}/addReview")
    public String addReview(@PathVariable Long recipeId, @RequestParam String comment, @RequestParam(required = false) Integer rating, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        if (rating == null) {
            throw new IllegalArgumentException("Rating is required");
        }

        Review review = new Review();
        review.setComment(comment);
        review.setRecipe(recipeService.findRecipeById(recipeId));
        review.setUser(loggedInUser);
        review.setRating(rating);

        System.out.println("Comment: " + comment);
        System.out.println("Rating: " + rating);

        reviewService.saveReview(review);

        return "redirect:/recipe/" + recipeId;
    }

    /**
     * Displays a review edit form.
     *
     * @param reviewId ID of the review to be edited
     * @param model model to hold attributes for the view
     * @return the URL to update the page
     */
    @RequestMapping("/review/{reviewId}/edit-review")
    public String editReview(@PathVariable Long reviewId, Model model) {
        Review review = reviewService.findReviewById(reviewId);
        if (review == null) {
            return "error";
        }
        model.addAttribute("review", review);
        return "/edit-review";
    }

    /**
     * Handles the submission of an edit-review form and updates review accordingly.
     *
     * @param updatedReview review object that contains the updated information
     * @param result binding result to validate the review data
     * @param model model to hold attributes for the view
     * @param session the HTTP session to retrieve logged-in user
     * @return redirect to the recipe the review was posted on
     */
    @RequestMapping(value = "/edit-review", method = RequestMethod.POST)
    public String updateReview(@ModelAttribute("review") Review updatedReview,
                               BindingResult result, Model model, HttpSession session) {
        Long reviewId = updatedReview.getId();
        Review currentReview = reviewService.findReviewById(reviewId);

        currentReview.setComment(updatedReview.getComment());
        currentReview.setRating(updatedReview.getRating());

        reviewService.updateReview(currentReview);
        return "redirect:/recipe/" + currentReview.getRecipe().getRecipeId();
    }

    /**
     * Deletes a review for a recipe.
     * This method allows a logged-in user to delete their own review for a specific recipe.
     * The method ensures the user is logged in and owns the review they are trying to delete.
     *
     * @param reviewId the ID of the review to be deleted
     * @param session  the HTTP session that contains the logged-in user
     * @return a redirect URL to the recipe page if the review is deleted, or an unauthorized page if the review does not belong to the logged-in user
     */
    @PostMapping("/review/{reviewId}/delete") // sama hér, erum við að nota review eða recipe...
    public String deleteReview(@PathVariable Long reviewId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Review review = reviewService.findReviewById(reviewId);
        if (loggedInUser != null && review.getUser().getUsername().equals(loggedInUser.getUsername())) {
            reviewService.deleteReviewById(reviewId);
        } else {
            return "redirect:/unauthorized";
        }

        return "redirect:/recipe/" + review.getRecipe().getRecipeId();
    }
}
