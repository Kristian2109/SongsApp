package bg.sofia.uni.fmi.mjt.spotify.server.application;

import java.util.Map;

public interface Logger {
    void logInfo(String message);

    void logError(String message);

    void logWarning(String message);

    void logInfoPairs(Map<String, Object> pairs);
}
