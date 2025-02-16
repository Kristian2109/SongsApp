package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.common.AudioFormatSerializable;
import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PlaySongCommand implements Command {
    private final SongsRepository songsRepository;
    private final String songName;
    private final Logger logger;

    public PlaySongCommand(SongsRepository repository, Logger logger, String songName) {
        this.songsRepository = repository;
        this.songName = songName;
        this.logger = logger;
    }
    @Override
    public Object execute() {
        Song song = songsRepository.getByName(songName).orElseThrow();
        File songPath = new File(song.getSource());
        int streamingPort = getFreePort();

        try {
            AudioFormat audioFormat = AudioSystem.getAudioInputStream(songPath).getFormat();
            song.incrementListings();
            songsRepository.updateOrThrow(song);
//            new Thread(() -> beginStreaming(songPath.getAbsolutePath(), streamingPort, logger)).start();
            return mapAudioToSerializable(audioFormat, song);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AudioFormatSerializable mapAudioToSerializable(AudioFormat audioFormat, Song song) {
        return new AudioFormatSerializable(
            audioFormat.getEncoding().toString(),
            audioFormat.getSampleRate(),
            audioFormat.getSampleSizeInBits(),
            audioFormat.getChannels(),
            audioFormat.getFrameSize(),
            audioFormat.getFrameRate(),
            audioFormat.isBigEndian(),
            song.getId()
        );
    }

    private int getFreePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return serverSocket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Cannot find free port");
        }
    }

    private void beginStreaming(String songPath, int streamingPort, Logger logger) {
        try (ServerSocket serverSocket = new ServerSocket(streamingPort);
             FileInputStream inputStream = new FileInputStream(songPath)) {
            Socket socket = serverSocket.accept();
            logger.logInfo("Streaming Began");

            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            logger.logInfo("Streaming ended");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
