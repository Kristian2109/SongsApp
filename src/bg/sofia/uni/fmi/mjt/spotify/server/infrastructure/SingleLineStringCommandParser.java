package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure;

import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.AddSongToPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.GetPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.PlaySongCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;

public class SingleLineStringCommandParser implements CommandParser {
    private final UserRepository userRepository;
    private final SongsRepository songsRepository;
    private final PlaylistRepository playlistRepository;

    public SingleLineStringCommandParser(UserRepository userRepository, SongsRepository songsRepository,
                                         PlaylistRepository playlistRepository) {
        this.userRepository = userRepository;
        this.songsRepository = songsRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public Command parse(String commandInput) {
        String[] tokens = commandInput.trim().split("\s+");

        if (tokens.length < 1) {
            throw new IllegalArgumentException("Invalid tokens provided");
        }

        return switch (tokens[0]) {
            case "add-song-to" ->
                new AddSongToPlaylistCommand(songsRepository, playlistRepository, tokens[1], tokens[2]);
            case "register" -> new RegisterCommand();
            case "login" -> new LoginCommand();
            case "create-playlist" -> new CreatePlaylistCommand();
            case "play" -> new PlaySongCommand();
            case "show-playlist" -> new GetPlaylistCommand();
            default -> throw new IllegalArgumentException("Invalid command " + tokens[0]);
        };
    }
}
