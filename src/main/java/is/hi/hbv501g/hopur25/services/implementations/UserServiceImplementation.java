package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.repositories.UserRepository;
import is.hi.hbv501g.hopur25.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User login(User user) {
        User doesExist = findByUsername(user.getUsername());
        System.out.println("User retrieved: " + doesExist);
        if (doesExist != null) {
            if (doesExist.getPassword().equals(user.getPassword())) {

                return doesExist;
            }
        }
        return null;
    }

    @Override
    public String updateUserSettings(User currentUser, User updatedUser) {

        if (!currentUser.getUsername().equals(updatedUser.getUsername())) {
            User doesExist = findByUsername(updatedUser.getUsername());
            if (doesExist != null) {
                return "Username taken";
            }
        }

        if (!currentUser.getEmail().equals(updatedUser.getEmail())) {
            User doesExist = findByEmail(updatedUser.getEmail());
            if (doesExist != null) {
                return "Email taken";
            }
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            currentUser.setPassword(updatedUser.getPassword());
        }

        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setEmail(updatedUser.getEmail());

        userRepository.save(currentUser);

        return null;
    }

    @Override
    public User addFavoriteRecipe(Long userId, Recipe recipe) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.getUserFavourites().add(recipe);  // Add the recipe to the user's favorites
            userRepository.save(user);  // Save the updated user to the database
        }
        return user;
    }

    @Override
    public User removeFavoriteRecipe(User user, Recipe recipe) {
        return null;
    }

    @Override
    public User removeFavoriteRecipe(Long userId, Recipe recipe) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getUserFavourites().remove(recipe);
        userRepository.save(user);
        return user;
    }


    @Override
    public List<Recipe> getUserFavorites(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return (user != null) ? user.getUserFavourites() : new ArrayList<>();
    }




    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

//
//
//    @Override
//    public void updatePicture(User user) {
//
//    }
//
//    @Override
//    public ArrayList<Recipe> getUserFavourites() {
//        return null;
//    }
//
//    @Override
//    public ArrayList<Recipe> getUserRecipes() {
//        return null;
//    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
