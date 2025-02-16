package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.common.AudioFormatSerializable;
import bg.sofia.uni.fmi.mjt.spotify.common.StreamingInfoServerResponse;
import com.google.gson.Gson;

import javax.sound.sampled.AudioFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;

public class Client {
    private static final String HOSTNAME = "localhost";
    private static final int SPOTIFY_SERVER_PORT = 3000;
    private final PlaySongService playSongService;
    private boolean isRunning = false;

    public Client(PlaySongService playSongService) {
        this.playSongService = playSongService;
    }

    public static void main(String[] args) {
        PlaySongService playService = new PlaySongService();
        Client client = new Client(playService);
        client.runClient();
    }

    private void runClient() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(HOSTNAME, SPOTIFY_SERVER_PORT));
            isRunning = true;
            System.out.println("Connected to the server.");

            while (isRunning) {
                handleActionCycle(reader, writer, scanner);
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    void handleActionCycle(BufferedReader reader, PrintWriter writer, Scanner scanner) throws IOException {
        System.out.print("Enter message: ");
        String message = scanner.nextLine();

        if ("disconnect".equals(message)) {
            playSongService.stopStreaming();
            isRunning = false;
            return;
        }

        if (message.equals("stop")) {
            Optional<String> streamingConnectionId = playSongService.getStreamingConnectionId();
            if (streamingConnectionId.isEmpty()) {
                System.out.println("No song");
                return;
            }
            message += " " + streamingConnectionId.get();
            playSongService.stopStreaming();

        }

        writer.println(message);
        String reply = reader.readLine();

        if (message.startsWith("play")) {
            handlePlayMusicResponse(reply);
        }

        System.out.println("The server replied <" + reply + ">");
    }

    void handlePlayMusicResponse(String reply) {
        Gson gson = new Gson();
        StreamingInfoServerResponse response = gson.fromJson(reply, StreamingInfoServerResponse.class);

        AudioFormatSerializable formatSerializable = response.result();
        AudioFormat format = formatSerializable.toAudioFormat();

        new Thread(() ->
            playSongService.startStreaming(format, formatSerializable.songId()), "Streaming"
        ).start();
    }
}
