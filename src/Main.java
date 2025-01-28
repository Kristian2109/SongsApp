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
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.SimpleClientInputHandler;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.SingleLineStringCommandParser;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserInMemoryRepository(User.class);
        SongsRepository songsRepository = new SongsInMemoryRepository();
        PlaylistRepository playlistRepository = new PlaylistInMemoryRepository();

        CommandParser commandParser = new SingleLineStringCommandParser(
            userRepository, songsRepository, playlistRepository
        );

        SocketAsynchronousServer server = new SocketAsynchronousServer(
            3000,
            new SimpleClientInputHandler(commandParser),
            new LocalLogger()
        );
        server.start();
    }
}