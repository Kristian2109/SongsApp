package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

public class Identifiable {
    public Identifiable(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Id;
}
