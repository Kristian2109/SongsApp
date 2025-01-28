import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging.LocalLogger;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SocketAsynchronousServer;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.SimpleClientInputHandler;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.SingleLineStringCommandParser;

public class Main {
    public static void main(String[] args) {
        CommandParser commandParser = new SingleLineStringCommandParser(userRepository, songsRepository,
            playlistRepository);
        SocketAsynchronousServer server = new SocketAsynchronousServer(3000,
            new SimpleClientInputHandler(commandParser),
            new LocalLogger());
        server.start();
    }
}