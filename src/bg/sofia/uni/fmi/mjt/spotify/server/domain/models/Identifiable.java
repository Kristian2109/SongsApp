package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

public class Identifiable {
    public Identifiable(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
}
