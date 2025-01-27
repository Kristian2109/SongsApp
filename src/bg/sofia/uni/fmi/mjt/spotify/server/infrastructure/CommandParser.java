package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure;

import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.Command;

public interface CommandParser {
    Command parse(String commandInput);
}
