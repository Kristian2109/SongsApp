package bg.sofia.uni.fmi.mjt.spotify.common;

import javax.sound.sampled.AudioFormat;

public record AudioFormatSerializable(
    String encoding,
    float sampleRate,
    int sampleSizeInBits,
    int channels,
    int frameSize,
    float frameRate,
    boolean bigEndian,
    String songId
) {
    public AudioFormat toAudioFormat() {
        return new AudioFormat(
            new AudioFormat.Encoding(encoding),
            sampleRate(),
            sampleSizeInBits(),
            channels(),
            frameSize(),
            frameRate(),
            bigEndian()
        );
    }
}
