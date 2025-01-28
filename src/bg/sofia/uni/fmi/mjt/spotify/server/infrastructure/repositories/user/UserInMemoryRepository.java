package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.BaseInMemoryRepository;

public class UserInMemoryRepository extends BaseInMemoryRepository<User> implements UserRepository {
    public UserInMemoryRepository(Class<User> aClass) {
        super(aClass);
    }
}
