package is.hi.hbv501g.hopur25.services;


import is.hi.hbv501g.hopur25.persistence.entities.User;

import java.util.List;

public interface UserService {
    User save(User user);

    void delete(User user);

    List<User> findAll();

    User findByUsername(String username);

    User login(User user);
}
