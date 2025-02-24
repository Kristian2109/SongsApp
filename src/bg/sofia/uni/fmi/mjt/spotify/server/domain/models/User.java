package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

import com.google.gson.annotations.Expose;

public class User extends Identifiable {
    @Expose(serialize = false)
    private final String passwordHash;
    private String email;

    public User(String id, String email, String passwordHash) {
        super(id);
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public boolean comparePassword(String hashedPassword) {
        return passwordHash.equals(hashedPassword);
    }
}
