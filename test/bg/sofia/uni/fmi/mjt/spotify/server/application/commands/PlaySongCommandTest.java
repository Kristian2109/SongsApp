package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.application.Logger;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlaySongCommandTest {
    private SongsRepository songsRepository;
    private Logger logger;
    private PlaySongCommand command;
    private final String songName = "Test Song";
    private final String songSource = "path/to/song.mp3";
    private final Song mockSong = new Song("1", songName, "Test Artist", songSource, 180);

    @BeforeEach
    void setUp() {
        songsRepository = mock(SongsRepository.class);
        logger = mock(Logger.class);
        command = new PlaySongCommand(songsRepository, logger, songName);
    }

    @Test
    void testExecuteThrowsRuntimeExceptionWhenAudioFileIsUnsupported() throws UnsupportedAudioFileException, IOException {
        try (MockedStatic<AudioSystem> mockedAudioSystem = Mockito.mockStatic(AudioSystem.class)) {
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(new File(songSource)))
                .thenThrow(UnsupportedAudioFileException.class);

            when(songsRepository.getByName(songName)).thenReturn(Optional.of(mockSong));

            assertThrows(RuntimeException.class, () -> command.execute());
            verify(songsRepository).getByName(songName);
        }
    }

    @Test
    void testExecuteThrowsRuntimeExceptionWhenIOExceptionOccurs() throws UnsupportedAudioFileException, IOException {
        try (MockedStatic<AudioSystem> mockedAudioSystem = Mockito.mockStatic(AudioSystem.class)) {
            mockedAudioSystem.when(() -> AudioSystem.getAudioInputStream(new File(songSource)))
                .thenThrow(IOException.class);

            when(songsRepository.getByName(songName)).thenReturn(Optional.of(mockSong));

            assertThrows(RuntimeException.class, () -> command.execute());
            verify(songsRepository).getByName(songName);
        }
    }

    @Test
    void testExecuteThrowsRuntimeExceptionWhenSongNotFound() {
        when(songsRepository.getByName(songName)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> command.execute());
        verify(songsRepository).getByName(songName);
    }
}