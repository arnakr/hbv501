package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User save(User user);
    void delete(User user);
    List<User> findAll();
    User findByUsername(String username);
    User findByEmail(String email);
    User login(User user);
    String updateUserSettings(User currentUser, User updatedUser);


//    void updatePicture(User user);
//    ArrayList<Recipe> getUserFavourites();
//    ArrayList<Recipe> getUserRecipes();
}
