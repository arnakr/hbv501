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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int size;

    private String userPicture;// Declare the userPicture field


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Recipe> userRecipes = new ArrayList<>();


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "user_favourites", // New table to hold favorite relationships
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> userFavourites = new ArrayList<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public List<Recipe> getUserRecipes() {
        return userRecipes;
    }

    public void setUserRecipes(List<Recipe> userRecipes) {
        this.userRecipes = userRecipes;
    }

    public List<Recipe> getUserFavourites() {
        return userFavourites;
    }

    public void setUserFavourites(List<Recipe> userFavourites) {
        this.userFavourites = userFavourites;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPicture() {
        return userPicture;  // Return the userPicture field
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;  // Assign the input value to the field
    }
}
