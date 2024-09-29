package is.hi.hbv501g.hopur25.persistence.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> userRecipes = new ArrayList<>();

    private List<Recipe> userFavourites = new ArrayList<>();
    private List<Review> userReviews = new ArrayList<>();

    private String username;
    private String email;
    private String password;
    private String userPicture;
}
