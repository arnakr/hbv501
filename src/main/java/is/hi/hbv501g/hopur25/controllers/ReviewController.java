package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.RecipeService;
import is.hi.hbv501g.hopur25.services.ReviewService;
import is.hi.hbv501g.hopur25.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReviewController {
    private final RecipeService recipeService;
    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(RecipeService recipeService, UserService userService, ReviewService reviewService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

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

        recipeService.saveReview(review);

        return "redirect:/recipe/" + recipeId;
    }

    @PostMapping("/review/{reviewId}/update") //á þetta að vera recipe/... í staðinn fyrir review? Erum ekki með neitt review
    public String updateReview(
            @PathVariable long reviewId,
            @RequestParam String comment,
            @RequestParam(required = false) Integer rating,
            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Review review = reviewService.findReviewById(reviewId);
        if (review == null || !review.getUser().equals(loggedInUser)) {
            return "redirect:/unauthorized";
        }

        review.setComment(comment);
        if (rating != null) {
            review.setRating(rating);
        }
        reviewService.saveReview(review);

        return "redirect:/recipe/" + review.getReview().getId();
    }

    @PostMapping("/review/{reviewId}/delete") // sama hér, erum við að nota review eða recipe...
    public String deleteReview(@PathVariable Long reviewId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("LoggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Review review = reviewService.findReviewById(reviewId);
        if (review == null || !review.getUser().equals(loggedInUser)) {
            return "redirect:/unauthorized";
        }

        reviewService.deleteReviewById(reviewId);

        return "redirect:/recipe/" + review.getRecipe().getRecipeId();
    }
}
