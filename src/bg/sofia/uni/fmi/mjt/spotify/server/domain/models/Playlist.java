package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

import java.util.Set;

public class Playlist extends Identifiable {
    private String name;
    private Set<Song> songs;

    public Playlist(String id, String name, Set<Song> songs) {
        super(id);
        this.name = name;
        this.songs = songs;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }
}
