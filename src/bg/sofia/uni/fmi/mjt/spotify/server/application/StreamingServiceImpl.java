package bg.sofia.uni.fmi.mjt.spotify.server.application;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class StreamingServiceImpl implements StreamingService {
    private static final int BUFFER_SIZE = 4096;
    private final Set<String> streamingConnectionIds;
    private final Logger logger;
    private final SongsRepository songsRepository;

    public StreamingServiceImpl(Set<String> streamingConnectionIds, Logger logger, SongsRepository songsRepository) {
        this.streamingConnectionIds = streamingConnectionIds;
        this.logger = logger;
        this.songsRepository = songsRepository;
    }

    @Override
    public void beginStreaming(Socket socket) {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            bytesRead = inputStream.read(buffer);
            String connectionId = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            streamingConnectionIds.add(connectionId);

            bytesRead = inputStream.read(buffer);
            String songId = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            Song song = this.songsRepository.getOrThrow(songId);
            logger.logInfo("Streaming running");

            FileInputStream fileInputStream = new FileInputStream(song.getSource());
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                if (!streamingConnectionIds.contains(connectionId)) {
                    break;
                }
                outputStream.write(buffer, 0, bytesRead);
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopStreaming(String connectionId) {
        streamingConnectionIds.remove(connectionId);
    }
}
