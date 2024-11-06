package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    void saveUser(User user);
    User save(User user);

    void deleteUser(Long userID);

    List<User> findAll();
    User findByUsername(String username);
    User findByEmail(String email);
    User login(User user);
    String updateUserSettings(User currentUser, User updatedUser);
    User addFavoriteRecipe(Long user, Recipe recipe);

    List<Recipe> getUserRecipes(Long userId);

    User findById(Long id);
    User removeFavoriteRecipe(Long userId, Recipe recipe);

    List<Recipe> getUserFavorites(Long userId); //Skoða

    List<Recipe> getUserRecipes(Long userId);

    User updateUserProfilePicture(Long userId, String profilePictureUrl);

//    void updatePicture(User user);
}
