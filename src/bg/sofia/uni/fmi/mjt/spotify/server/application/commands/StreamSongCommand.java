package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class StreamSongCommand implements Command {
    private final int streamingPort;

    private final String songId;
    private final SongsRepository songsRepository;

    public StreamSongCommand(SongsRepository songsRepository, String songId, int streamingPort) {
        this.streamingPort = streamingPort;
        this.songId = songId;
        this.songsRepository = songsRepository;
    }

    @Override
    public Object execute() {
        Song song = songsRepository.getOrThrow(songId);
        File songPath = new File(song.getSource());

        new Thread(() -> beginStreaming(songPath.getAbsolutePath())).start();

        return "Streaming Started";
    }

    private void beginStreaming(String songPath) {
        try (ServerSocket serverSocket = new ServerSocket(streamingPort);
             FileInputStream inputStream = new FileInputStream(songPath)) {
            Socket socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
