package bg.sofia.uni.fmi.mjt.spotify.client;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsolePresenter implements Presenter {
    private final Scanner scanner;
    private final PrintStream output;
    private final String CLEAR_CONSOLE_COMMAND = "\033[H\033[2J";

    public ConsolePresenter(Scanner scanner, PrintStream output) {
        this.scanner = scanner;
        this.output = output;
    }

    public ConsolePresenter() {
        this(new Scanner(System.in), System.out);
    }

    @Override
    public void writeMessage(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        output.println(message);
    }

    @Override
    public String getInput() {
        output.print("Enter input: ");
        return scanner.nextLine();
    }

    @Override
    public void clear() {
        for (int i = 0; i < 20; i++) {
            output.println();
        }
    }

    public void close() {
        scanner.close();
    }
}
