package bg.sofia.uni.fmi.mjt.spotify.server.domain.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
