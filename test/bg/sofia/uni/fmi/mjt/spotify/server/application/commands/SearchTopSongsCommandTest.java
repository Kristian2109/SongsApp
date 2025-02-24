package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchTopSongsCommandTest {
    private SongsRepository songsRepository;
    private SearchTopSongsCommand command;
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    void setUp() {
        songsRepository = mock(SongsRepository.class);
        song1 = mock(Song.class);
        song2 = mock(Song.class);
        song3 = mock(Song.class);

        when(song1.getListeningsCount()).thenReturn(200);
        when(song2.getListeningsCount()).thenReturn(300);
        when(song3.getListeningsCount()).thenReturn(100);
    }

    @Test
    void testExecuteReturnsTopNSongs() {
        when(songsRepository.getAll()).thenReturn(Set.of(song1, song2, song3));
        command = new SearchTopSongsCommand(songsRepository, 2);

        List<Song> result = command.execute();

        assertEquals(2, result.size());
        assertEquals(song2, result.get(0));
        assertEquals(song1, result.get(1));
    }

    @Test
    void testExecuteReturnsEmptyListWhenNoSongsAvailable() {
        when(songsRepository.getAll()).thenReturn(Set.of());
        command = new SearchTopSongsCommand(songsRepository, 3);

        List<Song> result = command.execute();

        assertTrue(result.isEmpty());
    }

    @Test
    void testExecuteReturnsAllSongsWhenLimitExceedsAvailableSongs() {
        when(songsRepository.getAll()).thenReturn(Set.of(song1, song2));
        command = new SearchTopSongsCommand(songsRepository, 5);

        List<Song> result = command.execute();

        assertEquals(2, result.size());
        assertEquals(song2, result.get(0));
        assertEquals(song1, result.get(1));
    }
}
