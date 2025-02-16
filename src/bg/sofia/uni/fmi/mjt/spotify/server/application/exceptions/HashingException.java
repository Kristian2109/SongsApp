package bg.sofia.uni.fmi.mjt.spotify.server.application.exceptions;

public class HashingException extends RuntimeException {
    public HashingException(String message) {
        super(message);
    }

    public HashingException(String message, Throwable cause) {
        super(message, cause);
    }
}
