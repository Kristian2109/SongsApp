package bg.sofia.uni.fmi.mjt.spotify.server.presentation;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class StreamingServer {
    public static StreamingServer streamingServer;
    private final int port;

    public static StreamingServer getInstance() {
        if (streamingServer == null) {
            throw new IllegalStateException("Streaming Server is not initialized");
        }
        return streamingServer;
    }

    private final Logger logger;
    private ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private final SongsRepository songsRepository;

    private final Set<String> streamingConnections;

    public static synchronized void initialize(
        Logger logger,
        ExecutorService threadPool,
        SongsRepository songsRepository,
        int port
    ) {
        if (streamingServer == null) {
            streamingServer = new StreamingServer(port, logger, threadPool, songsRepository);
        }
    }

    private StreamingServer(int port, Logger logger, ExecutorService threadPool, SongsRepository songsRepository) {
        this.port = port;
        this.logger = logger;
        this.threadPool = threadPool;
        this.songsRepository = songsRepository;
        this.streamingConnections = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void stopConnection(String connectionId) {
        streamingConnections.remove(connectionId);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            this.serverSocket = serverSocket;
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                threadPool.execute(() -> handleConnection(socket));
            }
        } catch (IOException e) {
            serverSocket = null;
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleConnection(Socket socket) {
        logger.logInfo("Streaming Began");

        try (OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream()) {
            String connectionId = socket.getRemoteSocketAddress().toString();
            streamingConnections.add(connectionId);

            byte[] buffer = new byte[4096];
            int bytesRead;

            bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) {
                logger.logInfo("No song provided");
                return;
            }

            String songId = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            Song song = this.songsRepository.getOrThrow(songId);

            FileInputStream fileInputStream = new FileInputStream(song.getSource());
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                if (!streamingConnections.contains(connectionId)) {
                    break;
                }
                outputStream.write(buffer, 0, bytesRead);
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.logInfo("Streaming ended");
    }
}
