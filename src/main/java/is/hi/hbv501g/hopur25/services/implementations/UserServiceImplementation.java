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

//    @Override
//    public void register(User user) {
//
//    }

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

//    @Override
//    public void logout() {
//
//    }
//
//    @Override
//    public void updateUsername(User user) {
//
//    }
//
//    @Override
//    public void updatePassword(User user) {
//
//    }
//
//    @Override
//    public void updateEmail(User user) {
//
//    }
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
