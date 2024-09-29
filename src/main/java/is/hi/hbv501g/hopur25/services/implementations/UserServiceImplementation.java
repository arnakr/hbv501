package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    private List<User> userRepository = new ArrayList<>();
    private int id_counter = 0;

    @Autowired
    public UserServiceImplementation() {
        userRepository.add(new User("User 1", "user1@hi.is", "u1"));
        userRepository.add(new User("User 2", "user2@hi.is", "u2"));

        for (User u: userRepository) {
            u.setUserID(id_counter);
            id_counter++;
        }
    }

    @Override
    public void register(User user) {

    }

    @Override
    public boolean login(User user) {
        return false;
    }

    @Override
    public void logout() {

    }

    @Override
    public void updateUsername(User user) {

    }

    @Override
    public void updatePassword(User user) {

    }

    @Override
    public void updateEmail(User user) {

    }

    @Override
    public void updatePicture(User user) {

    }

    @Override
    public ArrayList<Recipe> getUserFavourites() {
        return null;
    }

    @Override
    public ArrayList<Recipe> getUserRecipes() {
        return null;
    }

    @Override
    public List<User> findAll() {
        return userRepository;
    }
}
