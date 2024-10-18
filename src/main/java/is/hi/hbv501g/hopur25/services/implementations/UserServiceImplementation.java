package is.hi.hbv501g.hopur25.services.implementations;

import is.hi.hbv501g.hopur25.persistence.entities.Recipe;
import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.repositories.UserRepository;
import is.hi.hbv501g.hopur25.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if(doesExist != null){
            if(doesExist.getPassword().equals(user.getPassword())){

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
    public User addFavoriteRecipe(Long userId, Recipe recipe) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getUserFavourites().add(recipe); // Add recipe to favorites
            return userRepository.save(user); // Save updated user
        }
        return null; // Handle user not found case
    }

    @Override
    public User removeFavoriteRecipe(User user, Recipe recipe) {
        return null;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<Recipe> getUserFavorites(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getUserFavourites();
        }
        return null;
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
