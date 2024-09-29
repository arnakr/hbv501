package is.hi.hbv501g.hopur25.persistence.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private long userID;
    private List<Recipe> userRecipes = new ArrayList<>();
    private List<Recipe> userFavourites = new ArrayList<>();
    private List<Review> userReviews = new ArrayList<>();

    private String username;
    private String email;
    private String password;
    private String userPicture;

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

    public List<Review> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(List<Review> userReviews) {
        this.userReviews = userReviews;
    }
}
