package is.hi.hbv501g.hopur25.controllers;

import is.hi.hbv501g.hopur25.persistence.entities.User;
import is.hi.hbv501g.hopur25.persistence.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/users/view")
    public String getAllUsers(Model model) {
        System.out.println("Getting all users");
        // get all users from database
        List<User> users = userRepo.findAll();
        // end of database call
        model.addAttribute("us", users);
        return "users/showAll";
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response) {
        System.out.println("ADD user");
        String newName = newuser.get("name");
        String newPwd = newuser.get("password");
        int newSize = Integer.parseInt(newuser.get("size"));
        userRepo.save(new User(newName, newPwd, newSize));
        response.setStatus(201);
        return "users/addedUser";
    }
}
