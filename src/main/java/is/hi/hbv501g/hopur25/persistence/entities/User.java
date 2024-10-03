package is.hi.hbv501g.hopur25.persistence.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    private String username; // Changed from 'name' to 'username'
    private String password;
    private int size;

    public User() {
    }

    public User(String username, String password, int size) {
        this.username = username; // Update constructor
        this.password = password;
        this.size = size;
    }

    public String getUsername() {
        return username; // Update getter
    }

    public void setUsername(String username) {
        this.username = username; // Update setter
    }

    // Other getters and setters unchanged
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
