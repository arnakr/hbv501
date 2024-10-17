package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface UserService {

    User save(User user);
    void delete(User user);
    List<User> findAll();
    User findByUsername(String username);
    User findByEmail(String email);
    User login(User user);

//    User login(User user);
//    void logout();
//    void updateUsername(User user);
//    void updatePassword(User user);
//    void updateEmail(User user);
//    void updatePicture(User user);
//    ArrayList<Recipe> getUserFavourites();
//    ArrayList<Recipe> getUserRecipes();
}
