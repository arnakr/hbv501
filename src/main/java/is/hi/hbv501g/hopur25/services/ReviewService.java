package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Review;

public interface ReviewService {
    void saveReview(Review review);
    void deleteReview(Review review);
}

