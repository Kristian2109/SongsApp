package bg.sofia.uni.fmi.mjt.spotify.server.presentation;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.ClientInputHandler;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.Serializer;

import java.util.HashMap;
import java.util.Map;

public class SimpleClientInputHandler implements ClientInputHandler {
    private final CommandParser commandParser;
    private final Logger logger;
    private final Serializer serializer;

    public SimpleClientInputHandler(CommandParser commandParser, Logger logger, Serializer serializer) {
        this.commandParser = commandParser;
        this.logger = logger;
        this.serializer = serializer;
    }

    @Override
    public String handle(String commandInput) {
        Map<String, Object> jsonResult = new HashMap<>();
        try {
            Command command = commandParser.parse(commandInput);
            Object result = command.execute();
            jsonResult.put("result", result);
            jsonResult.put("success", true);
            logger.logInfo("Success");
            return serializer.serialize(jsonResult);
        } catch (RuntimeException e) {
            logger.logError("Error");
            jsonResult.put("error", e.getMessage());
            jsonResult.put("success", false);
            return serializer.serialize(jsonResult);
        }
    }
}
