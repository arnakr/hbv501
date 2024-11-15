package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.repositories.RecipeRepository;
import is.hi.hbv501g.hopur25.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService {
    private RecipeRepository recipeRepository;


    @Autowired
    public ReviewServiceImplementation(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveReview(Review review) {

    }

    @Override
    public List<Review> getReviews() {
        return List.of();
    }

    @Override
    public Review getReview(int id) {
        return null;
    }

    @Override
    public void deleteReview(int id) {

    }
}
