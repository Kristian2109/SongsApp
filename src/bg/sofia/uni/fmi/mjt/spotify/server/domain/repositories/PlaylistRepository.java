package bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;

import java.util.Optional;

public interface PlaylistRepository extends BaseRepository<Playlist> {
    Optional<Playlist> getByName(String name);
}
