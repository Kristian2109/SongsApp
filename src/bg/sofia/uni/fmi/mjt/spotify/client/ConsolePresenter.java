package bg.sofia.uni.fmi.mjt.spotify.client;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsolePresenter implements Presenter {
    private final Scanner scanner;
    private final PrintStream output;

    public ConsolePresenter(Scanner scanner, PrintStream output) {
        this.scanner = scanner;
        this.output = output;
    }

    @Override
    public void writeMessage(String message) {
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
