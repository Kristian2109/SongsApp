package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure;

import bg.sofia.uni.fmi.mjt.spotify.server.business.commands.Command;

public interface CommandParser {
    Command parse(String commandInput);
}
