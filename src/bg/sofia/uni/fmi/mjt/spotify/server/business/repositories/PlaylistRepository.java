package bg.sofia.uni.fmi.mjt.spotify.server.business.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.models.Playlist;

import java.util.Optional;

public interface PlaylistRepository {
    void update(Playlist playlist);
    Optional<Playlist> getByName(String name);
}
