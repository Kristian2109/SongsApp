package bg.sofia.uni.fmi.mjt.spotify.server.presentation;

import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.AddSongToPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.GetPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.PlaySongCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.SearchSongsCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;

import java.util.Arrays;
import java.util.Set;

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
            case "search" -> buildSongsCommand(tokens);
            case "register" -> new RegisterCommand();
            case "login" -> new LoginCommand();
            case "create-playlist" -> new CreatePlaylistCommand();
            case "play" -> new PlaySongCommand();
            case "show-playlist" -> new GetPlaylistCommand();
            default -> throw new IllegalArgumentException("Invalid command " + tokens[0]);
        };
    }

    private SearchSongsCommand buildSongsCommand(String[] tokens) {
        Set<String> keywords = Set.of(Arrays.copyOfRange(tokens, 1, tokens.length));
        return new SearchSongsCommand(songsRepository, keywords);
    }
}
