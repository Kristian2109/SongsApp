package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

import java.util.Set;

public record Playlist(String name, Set<Song> songs) {
}
