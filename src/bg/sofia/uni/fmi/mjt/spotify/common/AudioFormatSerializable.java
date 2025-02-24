package bg.sofia.uni.fmi.mjt.spotify.common;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;

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

    public static AudioFormatSerializable from(AudioFormat audioFormat, Song song) {
        return new AudioFormatSerializable(
            audioFormat.getEncoding().toString(),
            audioFormat.getSampleRate(),
            audioFormat.getSampleSizeInBits(),
            audioFormat.getChannels(),
            audioFormat.getFrameSize(),
            audioFormat.getFrameRate(),
            audioFormat.isBigEndian(),
            song.getId()
        );
    }
}
