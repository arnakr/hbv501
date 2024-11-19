package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);
    void deleteById(Long id);
    List<User> findAll();
    User findByUsername(String username);
    User findByEmail(String email);

    @Modifying
    @Query(value = "DELETE FROM user_favourites WHERE recipe_id = :recipeId", nativeQuery = true)
    void removeRecipeFromFavourites(@Param("recipeId") Long recipeId);
}
