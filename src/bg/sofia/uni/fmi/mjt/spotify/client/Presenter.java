package bg.sofia.uni.fmi.mjt.spotify.client;

public interface Presenter {
    void writeMessage(String message);
    String getInput();

    void clear();
}
