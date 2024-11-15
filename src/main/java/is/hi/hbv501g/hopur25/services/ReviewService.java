package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    void saveReview(Review review);
    List<Review> getReviews();
    Review getReview(int id);
    void deleteReview(int id);
}
