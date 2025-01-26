package bg.sofia.uni.fmi.mjt.spotify.server.business.commands;

import java.util.Map;

public interface Command {
    Map<String, Object> execute();
}
