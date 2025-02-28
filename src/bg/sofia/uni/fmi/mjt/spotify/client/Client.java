package bg.sofia.uni.fmi.mjt.spotify.client;

import java.io.PrintStream;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Presenter presenter = new ConsolePresenter(new Scanner(System.in), new PrintStream(System.out));
        PlaySongService playSongService = new PlaySongService();
        CommandHandler commandHandler = new CommandHandler(presenter, playSongService);
        SocketHandler socketHandler = new SocketHandler(commandHandler);
        socketHandler.run();
    }

}
