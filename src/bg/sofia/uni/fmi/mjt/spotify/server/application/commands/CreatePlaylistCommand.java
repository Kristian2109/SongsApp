package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;

import java.util.HashSet;

public class CreatePlaylistCommand implements Command {
    private final PlaylistRepository playlistRepository;
    private final String playlistName;

    public CreatePlaylistCommand(PlaylistRepository playlistRepository, String playlistName) {
        this.playlistRepository = playlistRepository;
        this.playlistName = playlistName;
    }

    @Override
    public Object execute() {
        Playlist newPlaylist = new Playlist("0", playlistName, new HashSet<>());
        return playlistRepository.create(newPlaylist);
    }
}
