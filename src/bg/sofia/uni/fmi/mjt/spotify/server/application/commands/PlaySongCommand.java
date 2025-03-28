package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.common.AudioFormatSerializable;
import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class PlaySongCommand implements Command {
    private final SongsRepository songsRepository;
    private final String songName;
    private final Logger logger;

    public PlaySongCommand(SongsRepository repository, Logger logger, String songName) {
        this.songsRepository = repository;
        this.songName = songName;
        this.logger = logger;
    }

    @Override
    public Object execute() {
        Song song = songsRepository.getByName(songName).orElseThrow();
        File songPath = new File(song.getSource());

        try {
            AudioFormat audioFormat = AudioSystem.getAudioInputStream(songPath).getFormat();
            song.incrementListings();
            songsRepository.updateOrThrow(song);
            return AudioFormatSerializable.from(audioFormat, song);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
