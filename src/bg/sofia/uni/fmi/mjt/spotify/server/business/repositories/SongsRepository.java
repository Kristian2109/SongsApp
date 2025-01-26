package bg.sofia.uni.fmi.mjt.spotify.server.business.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.models.Song;

import java.util.Optional;

public interface SongsRepository {
    Optional<Song> getByName(String name);
}
