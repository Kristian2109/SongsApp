package bg.sofia.uni.fmi.mjt.spotify.common;

public record AudioFormatSerializable(
    String encoding,
    float sampleRate,
    int sampleSizeInBits,
    int channels,
    int frameSize,
    float frameRate,
    boolean bigEndian,
    int port
) { }
