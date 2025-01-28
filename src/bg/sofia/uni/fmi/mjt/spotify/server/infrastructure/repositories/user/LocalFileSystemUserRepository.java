package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseInMemoryRepository;

public class LocalFileSystemUserRepository extends BaseInMemoryRepository<User> implements UserRepository {
    public LocalFileSystemUserRepository(Class<User> aClass) {
        super(aClass);
    }
}
