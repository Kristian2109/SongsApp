package bg.sofia.uni.fmi.mjt.spotify.client;

import bg.sofia.uni.fmi.mjt.spotify.common.ServerResponse;
import bg.sofia.uni.fmi.mjt.spotify.common.StreamingInfoServerResponse;
import com.google.gson.Gson;

import javax.sound.sampled.AudioFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class CommandHandler {
    private final Presenter presenter;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private boolean isLoggedIn = false;

    private boolean isRunning = true;

    private final PlaySongService playSongService;

    private final static Gson GSON = new Gson();

    public CommandHandler(Presenter presenter, PlaySongService playSongService) {
        this.presenter = presenter;
        this.playSongService = playSongService;
    }

    public void handle(BufferedReader reader, PrintWriter writer) throws IOException {
        renderMenu();
        String message = presenter.getInput();

        if ("disconnect".equals(message)) {
            playSongService.stopStreaming();
            isRunning = false;
            return;
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

        writer.println(message);
        String reply = reader.readLine();
        if (message.startsWith("play")) {
            handlePlayMusicResponse(reply);
        }

        ServerResponse response = GSON.fromJson(reply, ServerResponse.class);

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

    public boolean isRunning() { return isRunning; }

    void handlePlayMusicResponse(String response) {
        StreamingInfoServerResponse formatSerializable = GSON.fromJson(response, StreamingInfoServerResponse.class);
        AudioFormat format = formatSerializable.result().toAudioFormat();

        new Thread(() ->
            playSongService.startStreaming(format, formatSerializable.result().songId()), "Streaming"
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
