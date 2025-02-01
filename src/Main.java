import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging.LocalLogger;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.playlist.PlaylistInMemoryRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.songs.SongsInMemoryRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user.UserInMemoryRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SocketAsynchronousServer;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SimpleClientInputHandler;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SingleLineStringCommandParser;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserInMemoryRepository(User.class);
        SongsRepository songsRepository = new SongsInMemoryRepository(Song.class);
        songsRepository.create(new Song("0", "koy", "koy", "pesho"));
        songsRepository.create(new Song("1", "edno", "edno", "ivan"));
        PlaylistRepository playlistRepository = new PlaylistInMemoryRepository();

        CommandParser commandParser = new SingleLineStringCommandParser(
            userRepository, songsRepository, playlistRepository
        );
        Logger logger = new LocalLogger();

        SocketAsynchronousServer server = new SocketAsynchronousServer(
            3000,
            new SimpleClientInputHandler(commandParser, logger),
            logger
        );
        server.start();
    }
}