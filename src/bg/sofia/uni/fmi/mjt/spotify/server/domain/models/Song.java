package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

public class Song extends Identifiable {
    private String name;
    private String source;

    public Song(String id, String name, String source) {
        super(id);
        this.name = name;
        this.source = source;
    }
}
