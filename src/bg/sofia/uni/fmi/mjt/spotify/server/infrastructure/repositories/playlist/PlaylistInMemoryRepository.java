package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.playlist;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseInMemoryRepository;

import java.util.Optional;

public class PlaylistInMemoryRepository extends BaseInMemoryRepository<Playlist> implements PlaylistRepository {
    public PlaylistInMemoryRepository() {
        super(Playlist.class);
    }

    @Override
    public Optional<Playlist> getByName(String name) {
        return entities.values().stream()
            .filter(playlist -> playlist.getName().equalsIgnoreCase(name))
            .findFirst();
    }
}
