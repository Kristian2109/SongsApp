package bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;

import java.util.Optional;

public interface SongsRepository {
    Optional<Song> getByName(String name);
}
