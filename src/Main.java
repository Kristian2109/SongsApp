import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.Server;
import bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.SimpleClientInputHandler;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(3000, new SimpleClientInputHandler());
        server.start();
    }
}