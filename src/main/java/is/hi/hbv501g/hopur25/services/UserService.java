package is.hi.hbv501g.hopur25.services;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    void register(User user);
    boolean login(User user);
    void logout();
    void updateUsername(User user);
    void updatePassword(User user);
    void updateEmail(User user);
    void updatePicture(User user);
    ArrayList<Recipe> getUserFavourites();
    ArrayList<Recipe> getUserRecipes();

    List<User> findAll();
}
