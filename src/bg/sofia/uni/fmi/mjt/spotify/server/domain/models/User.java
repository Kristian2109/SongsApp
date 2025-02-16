package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

import com.google.gson.annotations.Expose;

public class User extends Identifiable {
    private String email;
    @Expose(serialize = false)
    private final String passwordHash;

    public User(String id, String email, String passwordHash) {
        super(id);
        this.email = email;
        this.passwordHash = passwordHash;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean comparePassword(String hashedPassword) {
        return passwordHash.equals(hashedPassword);
    }
}
