package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

public class User extends Identifiable {
    private String email;
    private String password;

    public User(String id, String email, String password) {
        super(id);
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
