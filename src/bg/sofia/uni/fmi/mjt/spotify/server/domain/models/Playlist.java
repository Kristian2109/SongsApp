package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

public class Playlist extends Identifiable {
    private String name;
    public Playlist(String id, String name) {
        super(id);
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
