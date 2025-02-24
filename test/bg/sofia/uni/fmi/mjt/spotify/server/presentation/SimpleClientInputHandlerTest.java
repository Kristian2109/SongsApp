package bg.sofia.uni.fmi.mjt.spotify.server.presentation;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.application.commands.Command;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.CommandParser;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

class SimpleClientInputHandlerTest {
    private CommandParser commandParser;
    private Logger logger;
    private Serializer serializer;
    private SimpleClientInputHandler clientInputHandler;

    @BeforeEach
    void setUp() {
        commandParser = mock(CommandParser.class);
        logger = mock(Logger.class);
        serializer = mock(Serializer.class);
        clientInputHandler = new SimpleClientInputHandler(commandParser, logger, serializer);
    }

    @Test
    void testHandleSuccessfullyExecutesCommandAndReturnsSerializedResult() {
        String commandInput = "test-command";
        Command command = mock(Command.class);
        Object commandResult = "command-result";
        String serializedResult = "{\"result\":\"command-result\",\"success\":true}";

        when(commandParser.parse(commandInput)).thenReturn(command);
        when(command.execute()).thenReturn(commandResult);
        when(serializer.serialize(Map.of("result", commandResult, "success", true)))
            .thenReturn(serializedResult);

        String result = clientInputHandler.handle(commandInput);

        assertEquals(serializedResult, result, "The serialized result should match the expected output.");
        verify(logger).logInfo("Success");
        verify(commandParser).parse(commandInput);
        verify(command).execute();
        verify(serializer).serialize(Map.of("result", commandResult, "success", true));
    }

    @Test
    void testHandleReturnsSerializedErrorWhenCommandExecutionFails() {
        String commandInput = "test-command";
        Command command = mock(Command.class);
        RuntimeException exception = mock(RuntimeException.class);
        when(exception.getMessage()).thenReturn("message");

        when(commandParser.parse(commandInput)).thenReturn(command);
        doThrow(exception).when(command).execute();

        clientInputHandler.handle(commandInput);

        verify(logger).logError("Error");
        verify(commandParser).parse(commandInput);
        verify(command).execute();
        verify(serializer).serialize(Map.of("error", exception.getMessage(), "success", false));
    }

    @Test
    void testHandleReturnsSerializedErrorWhenCommandParsingFails() {
        String commandInput = "invalid-command";
        RuntimeException exception = mock(RuntimeException.class);
        when(exception.getMessage()).thenReturn("Invalid command");

        when(commandParser.parse(commandInput)).thenThrow(exception);

        clientInputHandler.handle(commandInput);

        verify(logger).logError("Error");
        verify(commandParser).parse(commandInput);
        verify(serializer).serialize(Map.of("error", exception.getMessage(), "success", false));
    }
}