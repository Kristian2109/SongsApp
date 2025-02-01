package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.songs;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseInMemoryRepository;

import java.util.Optional;

public class SongsInMemoryRepository extends BaseInMemoryRepository<Song> implements SongsRepository{
    public SongsInMemoryRepository(Class aClass) {
        super(aClass);
    }

    @Override
    public Optional<Song> getByName(String name) {
        return entities.values().stream()
            .filter(song -> song.getName().equalsIgnoreCase(name))
            .findFirst();
    }
}
