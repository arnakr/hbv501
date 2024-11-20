package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.repositories.ReviewRepository;
import is.hi.hbv501g.hopur25.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing reviews.
 * This class provides methods for saving reviews and deleting reviews,
 */
@Service
public class ReviewServiceImplementation implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImplementation(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Saves a review in the repository.
     *
     * @param review the saved review
     */
    @Override
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    /**
     * Deletes a review in the repository.
     *
     * @param id of the deleted review
     */
    @Override
    public void deleteReviewById(long id) {
        reviewRepository.deleteById(id);
    }

    /**
     * Retrieves all reviews from the repository.
     * This method returns a list of all reviews stored in the database.
     *
     * @return a list of all reviews
     */
    @Override
    public List<Review> getReviews() {
        return reviewRepository.findAll();
    } //á eflaust að vera í ReviewServImp

    /**
     * Retrieves a specific review by its ID.
     * This method finds a review by its unique ID. If no review is found for the given ID,
     * it returns null.
     *
     * @param id the ID of the review to retrieve
     * @return the review with the given ID, or `null` if not found
     */
    @Override
    public Review getReview(long id) {
        return reviewRepository.findById((long) id).orElse(null);
    }

    /**
     * Finds a review in the repository.
     *
     * @param reviewId the id of the review
     */
    @Override
    public Review findReviewById(long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }
}

