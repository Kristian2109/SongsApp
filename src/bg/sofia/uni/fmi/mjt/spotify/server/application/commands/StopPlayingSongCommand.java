package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.application.StreamingService;

public class StopPlayingSongCommand implements Command {
    private final String connectionId;
    private final StreamingService streamingService;

    public StopPlayingSongCommand(String connectionId, StreamingService streamingService) {
        this.connectionId = connectionId;
        this.streamingService = streamingService;
    }

    @Override
    public Object execute() {
        streamingService.stopStreaming(connectionId);
        return "Connection Stopped";
    }
}
