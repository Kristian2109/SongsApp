package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.common.AudioFormatSerializable;
import bg.sofia.uni.fmi.mjt.spotify.common.ServerResponse;
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

public class Client {
    private static final String HOSTNAME = "localhost";
    private static final int SPOTIFY_SERVER_PORT = 3000;
    private final PlaySongService playSongService;
    private boolean isRunning = false;
    private final Presenter presenter;
    private boolean isLoggedIn = false;

    private final static Gson GSON = new Gson();

    public Client(PlaySongService playSongService, Presenter presenter) {
        this.playSongService = playSongService;
        this.presenter = presenter;
    }

    public static void main(String[] args) {
        PlaySongService playService = new PlaySongService();
        ConsolePresenter consolePresenter = new ConsolePresenter();
        Client client = new Client(playService, consolePresenter);
        client.runClient();
    }

    private void runClient() {
        try (SocketChannel socketChannel = SocketChannel.open();
             BufferedReader reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true)
        ) {

            socketChannel.connect(new InetSocketAddress(HOSTNAME, SPOTIFY_SERVER_PORT));
            isRunning = true;
            presenter.writeMessage("Client Started");

            while (isRunning) {
                handleActionCycle(reader, writer);
            }
        } catch (IOException e) {
            presenter.writeMessage("Error: " + e.getMessage());
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }

    void handleActionCycle(BufferedReader reader, PrintWriter writer) throws IOException {
        renderMenu();
        String message = presenter.getInput();

        if ("disconnect".equals(message)) {
            playSongService.stopStreaming();
            isRunning = false;
            return;
        }

        writer.println(message);
        String reply = reader.readLine();
        ServerResponse response = GSON.fromJson(reply, ServerResponse.class);

        if (message.startsWith("play")) {
            handlePlayMusicResponse(response);
        }

        if (message.equals("stop")) {
            Optional<String> streamingConnectionId = playSongService.getStreamingConnectionId();
            if (streamingConnectionId.isEmpty()) {
                presenter.writeMessage("No song");
                return;
            }
            message += " " + streamingConnectionId.get();
            playSongService.stopStreaming();
        }

        if (message.startsWith("login")) {
            if (response.success()) {
                isLoggedIn = true;
            }
        }
        presenter.writeMessage("");
        presenter.writeMessage("#####################################");
        presenter.writeMessage("The server replied <" + reply + ">");
        presenter.writeMessage("");
        presenter.writeMessage("#####################################");
    }

    void handlePlayMusicResponse(ServerResponse response) {
        AudioFormatSerializable formatSerializable = (AudioFormatSerializable) response.result();
        AudioFormat format = formatSerializable.toAudioFormat();

        new Thread(() ->
            playSongService.startStreaming(format, formatSerializable.songId()), "Streaming"
        ).start();
    }

    void renderMenu() {
        presenter.writeMessage("Enter some of the following commands");
        if (isLoggedIn) {
            presenter.writeMessage("disconnect");
            presenter.writeMessage("search <words>");
            presenter.writeMessage("top <number>");
            presenter.writeMessage("create-playlist <name_of_the_playlist>");
            presenter.writeMessage("add-song-to <name_of_the_playlist> <song>");
            presenter.writeMessage("show-playlist <name_of_the_playlist>");
            presenter.writeMessage("play <song>");
            presenter.writeMessage("stop");
        } else {
            presenter.writeMessage("login <email> <password>");
            presenter.writeMessage("register <email> <password>");
        }
    }
}
