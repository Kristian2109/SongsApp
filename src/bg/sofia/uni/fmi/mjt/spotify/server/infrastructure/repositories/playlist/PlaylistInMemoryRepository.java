package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.playlist;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;

import java.util.Optional;

public class PlaylistInMemoryRepository implements PlaylistRepository {
    @Override
    public void update(Playlist playlist) {

    }

    @Override
    public Optional<Playlist> getByName(String name) {
        return Optional.empty();
    }
}
