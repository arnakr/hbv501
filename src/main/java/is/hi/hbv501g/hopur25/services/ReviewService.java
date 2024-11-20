package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Review;

import java.util.List;

public interface ReviewService {
    void saveReview(Review review);
    void deleteReviewById(long id);
    List<Review> getReviews();
    Review getReview(long id);
    Review findReviewById(long reviewId);
    void updateReview(Review updatedReview);
    List<Review> getReviews();
    Review getReview(int id);
}

