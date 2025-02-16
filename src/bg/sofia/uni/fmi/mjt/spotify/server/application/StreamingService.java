package bg.sofia.uni.fmi.mjt.spotify.server.application;

import java.net.Socket;

public interface StreamingService {
    void beginStreaming(Socket socket);
    void stopStreaming(String connectionId);
}
