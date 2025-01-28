package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

import java.util.Set;

public class Playlist extends Identifiable {
    private String name;

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    private Set<Song> songs;
    public Playlist(String id, String name, Set<Song> songs) {
        super(id);
        this.name = name;
        this.songs = songs;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
