package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;

public class GetPlaylistCommand implements Command {
    private final PlaylistRepository playlistRepository;
    private final String playlistName;

    public GetPlaylistCommand(PlaylistRepository playlistRepository, String playlistName) {
        this.playlistRepository = playlistRepository;
        this.playlistName = playlistName;
    }

    @Override
    public Object execute() {
        return playlistRepository.getByName(playlistName)
            .orElseThrow(() -> new IllegalArgumentException("Invalid playlist name"));
    }
}
