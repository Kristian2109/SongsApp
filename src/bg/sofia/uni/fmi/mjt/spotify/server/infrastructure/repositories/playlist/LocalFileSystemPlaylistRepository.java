package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.playlist;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseLocalFileSystemRepository;

import java.util.Optional;

public class LocalFileSystemPlaylistRepository extends BaseLocalFileSystemRepository<Playlist> implements
    PlaylistRepository {
    public LocalFileSystemPlaylistRepository(String directory) {
        super(Playlist.class, directory);
    }

    @Override
    public Optional<Playlist> getByName(String name) {
        return entities.values().stream()
            .filter(playlist -> playlist.getName().equalsIgnoreCase(name))
            .findFirst();
    }
}
