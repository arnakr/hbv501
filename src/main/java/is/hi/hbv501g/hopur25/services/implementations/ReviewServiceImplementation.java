package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.Review;
import is.hi.hbv501g.hopur25.persistence.repositories.ReviewRepository;
import is.hi.hbv501g.hopur25.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * Finds a review in the repository.
     *
     * @param reviewId the id of the review
     */
    @Override
    public Review findReviewById(long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    /**
     * Updates an existing review in the repository.
     *
     * @param updatedReview the updated review to be saved
     */
    @Override
    public void updateReview(Review updatedReview) {
        reviewRepository.save(updatedReview);
    }
}

