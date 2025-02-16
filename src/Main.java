import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.SimpleHashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging.LocalLogger;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.playlist.LocalFileSystemPlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.songs.LocalFileSystemSongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user.LocalFileSystemUserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SimpleClientInputHandler;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SingleLineStringCommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SocketAsynchronousServer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LocalFileSystemUserRepository userRepository = new LocalFileSystemUserRepository("C:\\Users\\krist\\OneDrive\\Работен плот\\MJT\\SpotifyProject\\data\\users.json");
        LocalFileSystemSongsRepository songsRepository = new LocalFileSystemSongsRepository("C:\\Users\\krist\\OneDrive\\Работен плот\\MJT\\SpotifyProject\\data\\songs.json");
        LocalFileSystemPlaylistRepository playlistRepository = new LocalFileSystemPlaylistRepository("C:\\Users\\krist\\OneDrive\\Работен плот\\MJT\\SpotifyProject\\data\\playlists.json");
        Logger logger = new LocalLogger();
        SimpleHashingService hashingService = new SimpleHashingService();

        CommandParser commandParser = new SingleLineStringCommandParser(
            userRepository, songsRepository, playlistRepository, logger, hashingService
        );

        SocketAsynchronousServer server = new SocketAsynchronousServer(
            3000,
            new SimpleClientInputHandler(commandParser, logger),
            logger
        );
//
//        StreamingServer.initialize(logger, Executors.newCachedThreadPool(), songsRepository, 8000);
//        StreamingServer streamingServer = StreamingServer.getInstance();
//        streamingServer.start();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            boolean isRunning = true;
            while (isRunning) {
                String command = scanner.nextLine().trim();
                if (command.equals("stop")) {
                    server.stop();
//                    streamingServer.stop();
                    isRunning = false;
                }
            }
        }).start();

        server.start();
        songsRepository.saveEntitiesToFileSystem();
        userRepository.saveEntitiesToFileSystem();
        playlistRepository.saveEntitiesToFileSystem();
    }
}