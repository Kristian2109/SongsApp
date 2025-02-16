package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.songs;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseLocalFileSystemRepository;

import java.nio.file.Path;
import java.util.Optional;

public class LocalFileSystemSongsRepository extends BaseLocalFileSystemRepository<Song> implements SongsRepository {
    public LocalFileSystemSongsRepository(Path sourceFile) {
        super(Song.class, sourceFile);
    }

    @Override
    public Optional<Song> getByName(String name) {
        return entities.values().stream()
            .filter(song -> song.getName().equalsIgnoreCase(name))
            .findFirst();
    }
}
