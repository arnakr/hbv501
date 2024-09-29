package is.hi.hbv501g.hopur25.persistence.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeID;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private List<Review> reviews = new ArrayList<>();

    private String title;
    private List<String> ingredients = new ArrayList<String>();
    private int cookTime;
    private String description;


}

