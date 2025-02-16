package bg.sofia.uni.fmi.mjt.spotify.server.presentation;

import bg.sofia.uni.fmi.mjt.spotify.server.application.HashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.AddSongToPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.CreatePlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.GetPlaylistCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.LoginCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.PlaySongCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.SearchSongsCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.SearchTopSongsCommand;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.StopPlayingSongCommand;
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
    private final Logger logger;
    private final HashingService hashingService;
    public SingleLineStringCommandParser(UserRepository userRepository, SongsRepository songsRepository,
                                         PlaylistRepository playlistRepository, Logger logger,
                                         HashingService hashingService) {
        this.userRepository = userRepository;
        this.songsRepository = songsRepository;
        this.playlistRepository = playlistRepository;
        this.logger = logger;
        this.hashingService = hashingService;
    }

    @Override
    public Command parse(String commandInput) {
        String[] tokens = commandInput.trim().split("\s+");

        if (tokens.length < 1) {
            throw new IllegalArgumentException("Invalid tokens provided");
        }

        return switch (tokens[0]) {
            case "add-song-to" ->
                new AddSongToPlaylistCommand(songsRepository, playlistRepository, tokens[2], tokens[1]);
            case "search" -> buildSongsCommand(tokens);
            case "register" -> new RegisterCommand(userRepository, hashingService , tokens[1], tokens[2]);
            case "login" -> new LoginCommand(userRepository, hashingService, tokens[1], tokens[2]);
            case "create-playlist" -> new CreatePlaylistCommand(playlistRepository, tokens[1]);
            case "play" -> new PlaySongCommand(songsRepository, logger, tokens[1]);
            case "show-playlist" -> new GetPlaylistCommand(playlistRepository, tokens[1]);
            case "top" -> new SearchTopSongsCommand(songsRepository, Integer.parseInt(tokens[1]));
            case "stop" -> new StopPlayingSongCommand(tokens[1]);
            default -> throw new IllegalArgumentException("Invalid command " + tokens[0]);
        };
    }

    private SearchSongsCommand buildSongsCommand(String[] tokens) {
        Set<String> keywords = Set.of(Arrays.copyOfRange(tokens, 1, tokens.length));
        return new SearchSongsCommand(songsRepository, keywords);
    }
}
