package bg.sofia.uni.fmi.mjt.spotify.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SocketHandler {
    private static final String HOSTNAME = "localhost";
    private static final int SPOTIFY_SERVER_PORT = 3000;

    private final CommandHandler commandHandler;

    public SocketHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void run() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true)
        ) {
            socketChannel.connect(new InetSocketAddress(HOSTNAME, SPOTIFY_SERVER_PORT));

            while (commandHandler.isRunning()) {
                commandHandler.handle(reader, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
