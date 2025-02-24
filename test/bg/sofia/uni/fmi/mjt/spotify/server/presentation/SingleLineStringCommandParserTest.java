package bg.sofia.uni.fmi.mjt.spotify.server.presentation;

import bg.sofia.uni.fmi.mjt.spotify.server.application.HashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.application.StreamingService;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SingleLineStringCommandParserTest {
    private UserRepository userRepository;
    private SongsRepository songsRepository;
    private PlaylistRepository playlistRepository;
    private StreamingService streamingService;
    private Logger logger;
    private HashingService hashingService;
    private SingleLineStringCommandParser commandParser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        songsRepository = mock(SongsRepository.class);
        playlistRepository = mock(PlaylistRepository.class);
        streamingService = mock(StreamingService.class);
        logger = mock(Logger.class);
        hashingService = mock(HashingService.class);

        commandParser = new SingleLineStringCommandParser(
            userRepository, songsRepository, playlistRepository, streamingService, logger, hashingService
        );
    }

    @Test
    void testParseAddSongToPlaylistCommand() {
        String input = "add-song-to playlistName songName";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof AddSongToPlaylistCommand, "Expected AddSongToPlaylistCommand");
    }

    @Test
    void testParseSearchSongsCommand() {
        String input = "search keyword1 keyword2";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof SearchSongsCommand, "Expected SearchSongsCommand");
    }

    @Test
    void testParseRegisterCommand() {
        String input = "register email password";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof RegisterCommand, "Expected RegisterCommand");
    }

    @Test
    void testParseLoginCommand() {
        String input = "login email password";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof LoginCommand, "Expected LoginCommand");
    }

    @Test
    void testParseCreatePlaylistCommand() {
        String input = "create-playlist playlistName";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof CreatePlaylistCommand, "Expected CreatePlaylistCommand");
    }

    @Test
    void testParsePlaySongCommand() {
        String input = "play songName";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof PlaySongCommand, "Expected PlaySongCommand");
    }

    @Test
    void testParseGetPlaylistCommand() {
        String input = "show-playlist playlistName";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof GetPlaylistCommand, "Expected GetPlaylistCommand");
    }

    @Test
    void testParseSearchTopSongsCommand() {
        String input = "top 5";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof SearchTopSongsCommand, "Expected SearchTopSongsCommand");
    }

    @Test
    void testParseStopPlayingSongCommand() {
        String input = "stop connectionId";
        Command command = commandParser.parse(input);

        assertTrue(command instanceof StopPlayingSongCommand, "Expected StopPlayingSongCommand");
    }

    @Test
    void testParseInvalidCommandThrowsException() {
        String input = "invalid-command";
        assertThrows(IllegalArgumentException.class, () -> commandParser.parse(input),
            "Expected IllegalArgumentException for invalid command");
    }

    @Test
    void testParseEmptyInputThrowsException() {
        String input = "";
        assertThrows(IllegalArgumentException.class, () -> commandParser.parse(input),
            "Expected IllegalArgumentException for empty input");
    }
}