package bg.sofia.uni.fmi.mjt.spotify.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

public class PlaySongService {
    private static final int STREAMING_SERVER_PORT = 8000;
    private static final int STREAMING_BUFFER_SIZE = 4096;
    private static final String HOSTNAME = "localhost";

    private Optional<String> streamingConnectionId;

    public PlaySongService() {
        this.streamingConnectionId = Optional.empty();
    }

    public void startStreaming(AudioFormat audioFormat, String songId) {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        try (SocketChannel socketChannel = SocketChannel.open();
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(info);
             AudioInputStream audioInputStream = new AudioInputStream(
                 Channels.newInputStream(socketChannel), audioFormat, AudioSystem.NOT_SPECIFIED
             )
        ) {
            socketChannel.connect(new InetSocketAddress(HOSTNAME, STREAMING_SERVER_PORT));
            streamingConnectionId = Optional.of(UUID.randomUUID().toString());

            sendInitialInfo(songId, writer);

            dataLine.open();
            dataLine.start();

            byte[] bufferBytes = new byte[STREAMING_BUFFER_SIZE];
            int readBytes;
            while ((readBytes = audioInputStream.read(bufferBytes)) != -1) {
                if (streamingConnectionId.isEmpty()) {
                    socketChannel.close();
                    dataLine.stop();
                    dataLine.close();
                    break;
                }
                dataLine.write(bufferBytes, 0, readBytes);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void sendInitialInfo(String songId, PrintWriter writer) {
        writer.write(streamingConnectionId.get());
        writer.flush();

        writer.write(songId);
        writer.flush();
    }

    public Optional<String> getStreamingConnectionId() {
        return streamingConnectionId;
    }

    public void stopStreaming() {
        streamingConnectionId = Optional.empty();
    }
}
