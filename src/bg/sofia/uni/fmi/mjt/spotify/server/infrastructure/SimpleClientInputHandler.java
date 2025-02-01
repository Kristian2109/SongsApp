package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.Command;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SimpleClientInputHandler implements ClientInputHandler {
    private final CommandParser commandParser;
    private final Logger logger;

    public SimpleClientInputHandler(CommandParser commandParser, Logger logger) {
        this.commandParser = commandParser;
        this.logger = logger;
    }

    @Override
    public String handle(String commandInput) {
        Gson gson = new Gson();
        Map<String, Object> jsonResult = new HashMap<>();
        try {
            Command command = commandParser.parse(commandInput);
            Object result = command.execute();
            jsonResult.put("result", result);
            jsonResult.put("success", true);
            return gson.toJson(jsonResult);
        } catch (RuntimeException e) {
            jsonResult.put("error", e.getMessage());
            return gson.toJson(jsonResult);
        }
    }
}
