package bg.sofia.uni.fmi.mjt.spotify.server.presentation;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.application.StreamingService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class StreamingServer {
    private final StreamingService streamingService;
    private final int port;
    private final Logger logger;
    private ServerSocket serverSocket;
    private final ExecutorService threadPool;

    public StreamingServer(StreamingService streamingService, int port, Logger logger, ExecutorService threadPool) {
        this.port = port;
        this.streamingService = streamingService;
        this.threadPool = threadPool;
        this.logger = logger;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
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

    private void handleConnection(Socket socket) {
        logger.logInfo("Streaming Began");

        try {
            streamingService.beginStreaming(socket);
        } catch (Exception e) {
            logger.logInfoPairs(Map.of("message", e.getMessage(), "stack", Arrays.toString(e.getStackTrace())));
        }

        logger.logInfo("Streaming ended");
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
}
