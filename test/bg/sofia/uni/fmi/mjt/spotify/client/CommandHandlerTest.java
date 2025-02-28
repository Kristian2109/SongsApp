package bg.sofia.uni.fmi.mjt.spotify.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandHandlerTest {
    private Presenter presenter;
    private PlaySongService playSongService;
    private BufferedReader reader;
    private PrintWriter writer;
    private CommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        presenter = mock(Presenter.class);
        playSongService = mock(PlaySongService.class);
        reader = mock(BufferedReader.class);
        writer = mock(PrintWriter.class);
        commandHandler = new CommandHandler(presenter, playSongService);
    }


    @Test
    void rendersMenuWhenNotLoggedIn() throws IOException {
        when(presenter.getInput()).thenReturn("");
        when(reader.readLine()).thenReturn("");

        commandHandler.handle(reader, writer);
        verify(presenter).writeMessage("Enter some of the following commands");
        verify(presenter).writeMessage("login <email> <password>");
        verify(presenter).writeMessage("register <email> <password>");
    }

    @Test
    void rendersMenuWhenLoggedIn() throws IOException {
        commandHandler.setLoggedIn(true);
        when(presenter.getInput()).thenReturn("");
        when(reader.readLine()).thenReturn("");

        commandHandler.handle(reader, writer);
        verify(presenter).writeMessage("Enter some of the following commands");
        verify(presenter).writeMessage("disconnect");
        verify(presenter).writeMessage("search <words>");
        verify(presenter).writeMessage("top <number>");
        verify(presenter).writeMessage("create-playlist <name_of_the_playlist>");
        verify(presenter).writeMessage("add-song-to <name_of_the_playlist> <song>");
        verify(presenter).writeMessage("show-playlist <name_of_the_playlist>");
        verify(presenter).writeMessage("play <song>");
        verify(presenter).writeMessage("stop");
    }

    @Test
    void testDisconnect() throws IOException {
        when(presenter.getInput()).thenReturn("disconnect");
        commandHandler.handle(reader, writer);
        assertFalse(commandHandler.isRunning());
    }

    @Test
    void testLogin() throws IOException {
        when(presenter.getInput()).thenReturn("login hey 123");
        when(reader.readLine()).thenReturn("{\"success\":true}");
        commandHandler.handle(reader, writer);
        assertTrue(commandHandler.isLoggedIn());
    }
}