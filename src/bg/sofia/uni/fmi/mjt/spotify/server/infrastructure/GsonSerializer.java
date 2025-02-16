package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure;

import com.google.gson.Gson;

public class GsonSerializer implements Serializer {
    private final Gson serializer;

    public GsonSerializer(Gson serializer) {
        this.serializer = serializer;
    }

    @Override
    public String serialize(Object o) {
        return serializer.toJson(o);
    }
}
