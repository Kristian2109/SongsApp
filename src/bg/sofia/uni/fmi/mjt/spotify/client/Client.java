package bg.sofia.uni.fmi.mjt.spotify.client;

public class Client {
    public static void main(String[] args) {
        Presenter presenter = new ConsolePresenter();
        PlaySongService playSongService = new PlaySongService();
        CommandHandler commandHandler = new CommandHandler(presenter, playSongService);
        SocketHandler socketHandler = new SocketHandler(presenter, commandHandler);
        socketHandler.run();
    }

}
