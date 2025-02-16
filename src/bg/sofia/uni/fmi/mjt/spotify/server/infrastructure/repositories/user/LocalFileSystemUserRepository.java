package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseLocalFileSystemRepository;

import java.nio.file.Path;
import java.util.Optional;

public class LocalFileSystemUserRepository extends BaseLocalFileSystemRepository<User> implements UserRepository {
    public LocalFileSystemUserRepository(Path filePath) {
        super(User.class, filePath);
    }

    @Override
    public Optional<User> getByEmail(String emailAddress) {
        return entities.values().stream()
            .filter(u -> u.getEmail().equals(emailAddress))
            .findFirst();
    }
}
