package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.songs;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import java.util.Optional;

public class SongsInMemoryRepository implements SongsRepository {
    @Override
    public Optional<Song> getByName(String name) {
        return Optional.empty();
    }
}
