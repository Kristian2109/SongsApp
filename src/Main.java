import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.application.StreamingServiceImpl;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.GsonSerializer;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.Serializer;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.SimpleHashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.logging.LocalLogger;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.playlist.LocalFileSystemPlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.songs.LocalFileSystemSongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories.user.LocalFileSystemUserRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.ExcludeSerializeFalseStrategy;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SimpleClientInputHandler;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SingleLineStringCommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.SpotifyServer;
import bg.sofia.uni.fmi.mjt.spotify.server.presentation.StreamingServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;

public class Main {
    private static final String DATA_DIRECTORY = Paths.get("data").toAbsolutePath().toString();
    private static final String USERS_FILE = "users.json";
    private static final String SONGS_FILE = "songs.json";
    private static final String PLAYLISTS_FILE = "playlists.json";
    private static final String LOGS_FILE = "logs.json";
    private static final int SPOTIFY_SERVER_PORT = 3000;
    private static final int STREAMING_SERVER_PORT = 8000;
    public static void main(String[] args) throws IOException {
        LocalFileSystemUserRepository userRepository = new LocalFileSystemUserRepository(
            Path.of(DATA_DIRECTORY, USERS_FILE));
        LocalFileSystemSongsRepository songsRepository = new LocalFileSystemSongsRepository(
            Path.of(DATA_DIRECTORY, SONGS_FILE));
        LocalFileSystemPlaylistRepository playlistRepository = new LocalFileSystemPlaylistRepository(
            Path.of(DATA_DIRECTORY, PLAYLISTS_FILE));

        Gson gson = new GsonBuilder()
            .addSerializationExclusionStrategy(new ExcludeSerializeFalseStrategy())
            .create();

        Serializer serializer = new GsonSerializer(gson);
        Logger logger = new LocalLogger(serializer, new FileWriter(new File(DATA_DIRECTORY, LOGS_FILE)));
        SimpleHashingService hashingService = new SimpleHashingService();

        Set<String> connectionIds = new ConcurrentSkipListSet<>();
        StreamingServiceImpl streamingService = new StreamingServiceImpl(connectionIds, logger, songsRepository);
        CommandParser commandParser = new SingleLineStringCommandParser(
            userRepository, songsRepository, playlistRepository, streamingService, logger, hashingService
        );

        SpotifyServer spotifyServer = new SpotifyServer(SPOTIFY_SERVER_PORT,
            new SimpleClientInputHandler(commandParser, logger, serializer),
            logger);

        StreamingServer streamingServer = new StreamingServer(streamingService, STREAMING_SERVER_PORT, logger,
            Executors.newFixedThreadPool(30));

        new Thread(() -> runAdminConsole(spotifyServer, streamingServer)).start();
        new Thread(streamingServer::start).start();
        spotifyServer.start();

        songsRepository.saveEntitiesToFileSystem();
        userRepository.saveEntitiesToFileSystem();
        playlistRepository.saveEntitiesToFileSystem();
    }

    private static void runAdminConsole(SpotifyServer spotifyServer, StreamingServer streamingServer) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while (isRunning) {
            String command = scanner.nextLine().trim();
            if (command.equals("stop")) {
                spotifyServer.stop();
                streamingServer.stop();
                isRunning = false;
            }
        }
    }
}