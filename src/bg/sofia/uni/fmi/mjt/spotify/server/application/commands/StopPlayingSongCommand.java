package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.presentation.StreamingServer;

public class StopPlayingSongCommand implements Command {
    private final String connectionId;

    public StopPlayingSongCommand(String connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public Object execute() {
        StreamingServer server = StreamingServer.getInstance();
        server.stopConnection(connectionId);
        return "Connection Stopped";
    }
}
