package bg.sofia.uni.fmi.mjt.spotify.server.application;

public interface Logger {
    void logInfo(String message);

    void logError(String message);

    void logWarning(String message);
}
