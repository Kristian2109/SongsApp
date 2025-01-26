package bg.sofia.uni.fmi.mjt.spotify.server.models;

import java.util.Set;

public record Playlist(String name, Set<Song> songs) {
}
