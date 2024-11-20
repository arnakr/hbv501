package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link Review} entities.
 * This interface extends the {@link JpaRepository} interface,
 * providing methods for operations for review concepts
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.rating = :rating")
    List<Review> findByRating(@Param("rating") int rating);

}
