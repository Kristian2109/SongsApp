package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class LocalLogger implements Logger {
    private Gson gson;
    private void log(String message) {
        Map<String, Object> logResult = new HashMap<>();
        logResult.put("message", message);
        logResult.put("time", LocalDateTime.now());
        System.out.println(gson.toJson(logResult));
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
}
