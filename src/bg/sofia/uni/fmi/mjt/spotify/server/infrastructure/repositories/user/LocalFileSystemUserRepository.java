package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseLocalFileSystemRepository;

public class LocalFileSystemUserRepository extends BaseLocalFileSystemRepository<User> implements UserRepository {
    public LocalFileSystemUserRepository(String filePath) {
        super(User.class, filePath);
    }
}
