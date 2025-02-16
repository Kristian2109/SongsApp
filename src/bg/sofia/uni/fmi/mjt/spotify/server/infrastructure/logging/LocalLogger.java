package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.Serializer;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class LocalLogger implements Logger {
    private final Serializer serializer;
    private final FileWriter fileWriter;
    public LocalLogger(Serializer serializer, FileWriter fileWriter) {
        this.serializer = serializer;
        this.fileWriter = fileWriter;
    }

    private void log(String message) {
        logInfoPairs(Map.of("message", message, "time", LocalDateTime.now().toString()));
    }

    @Override
    public void logInfo(String message) {
        log(message);
    }

    @Override
    public void logError(String message) {
        log(message);
    }

    @Override
    public void logWarning(String message) {
        log(message);
    }

    @Override
    public void logInfoPairs(Map<String, Object> pairs) {
        try {
            String log = serializer.serialize(pairs);
            fileWriter.append(log);
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("Could not log");
        }
    }
}
