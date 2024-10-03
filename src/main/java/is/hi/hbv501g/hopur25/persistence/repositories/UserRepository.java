package is.hi.hbv501g.hopur25.persistence.repositories;

import is.hi.hbv501g.hopur25.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findBySize(int size);

    List<User> findByNameAndPassword(String name, String password);

    // Keep this method to find by username
    User findByUsername(String username);
}
