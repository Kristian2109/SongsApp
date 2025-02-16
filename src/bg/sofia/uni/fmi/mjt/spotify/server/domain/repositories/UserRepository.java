package bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    Optional<User> getByEmail(String emailAddress);
}
