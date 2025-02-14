import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging.LocalLogger;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.playlist.PlaylistInMemoryRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.songs.LocalFileSystemSongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user.UserInMemoryRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SimpleClientInputHandler;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SingleLineStringCommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SocketAsynchronousServer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserInMemoryRepository userRepository = new UserInMemoryRepository(User.class);
        LocalFileSystemSongsRepository songsRepository = new LocalFileSystemSongsRepository("C:\\Users\\krist\\OneDrive\\Работен плот\\MJT\\SpotifyProject\\data\\songs.json");
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

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            boolean isRunning = true;
            while (isRunning) {
                String command = scanner.nextLine().trim();
                if (command.equals("stop")) {
                    server.stop();
                    isRunning = false;
                }
            }
        }).start();

        server.start();
        songsRepository.saveEntitiesToFileSystem();
    }
}