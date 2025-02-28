package bg.sofia.uni.fmi.mjt.spotify.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsolePresenterTest {
    private Scanner scanner;
    private ByteArrayOutputStream outputStream;
    private ConsolePresenter consolePresenter;

    @BeforeEach
    void setUp() {
        scanner = mock(Scanner.class);
        outputStream = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(outputStream);
        consolePresenter = new ConsolePresenter(scanner, output);
    }

    @Test
    void testWriteMessagePrintsMessageToOutput() {
        String message = "Test message";

        consolePresenter.writeMessage(message);

        assertEquals(message + System.lineSeparator(), outputStream.toString(),
            "The message should be printed to the output stream.");
    }

    @Test
    void testGetInputPromptsUserAndReturnsInput() {
        String userInput = "user input";
        when(scanner.nextLine()).thenReturn(userInput);

        String result = consolePresenter.getInput();

        assertEquals(userInput, result, "The returned input should match the user input.");
        verify(scanner).nextLine();
    }

    @Test
    void testClearPrintsBlankLines() {
        consolePresenter.clear();

        String expectedOutput = String.valueOf(System.lineSeparator()).repeat(20);
        assertEquals(expectedOutput, outputStream.toString(),
            "The output should contain 20 blank lines.");
    }

    @Test
    void testCloseClosesScanner() {
        consolePresenter.close();

        verify(scanner).close();
    }
}