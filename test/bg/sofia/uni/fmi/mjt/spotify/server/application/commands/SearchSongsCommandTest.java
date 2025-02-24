package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchSongsCommandTest {
    private SongsRepository songsRepository;
    private SearchSongsCommand command;
    private final Set<String> keywords = Set.of("test", "song");

    @BeforeEach
    void setUp() {
        songsRepository = mock(SongsRepository.class);
        command = new SearchSongsCommand(songsRepository, keywords);
    }

    @Test
    void testExecuteReturnsFilteredSongsWhenKeywordsMatch() {
        Song song1 = new Song("1", "Test Song", "path1", "Artist1",180);
        Song song2 = new Song("2", "Another Song", "path2", "Artist2", 200);
        Song song3 = new Song("3", "No Match", "path3","Artist3",  150);

        when(songsRepository.getAll()).thenReturn(Set.of(song1, song2, song3));

        Set<Song> result = command.execute();

        assertEquals(2, result.size());
        assertEquals(Set.of(song1, song2), result);
    }

    @Test
    void testExecuteReturnsEmptyListWhenNoKeywordsMatch() {
        Song song1 = new Song("1", "No Match 1", "path1","Artist1",  180);
        Song song2 = new Song("2", "No Match 2", "path2","Artist2",  200);

        when(songsRepository.getAll()).thenReturn(Set.of(song1, song2));

        Set<Song> result =  command.execute();

        assertEquals(0, result.size());
    }

    @Test
    void testExecuteReturnsEmptyListWhenNoSongsExist() {
        when(songsRepository.getAll()).thenReturn(Set.of());

        Set<Song> result = command.execute();

        assertEquals(0, result.size());
    }

    @Test
    void testExecuteReturnsFilteredSongsWhenKeywordsMatchSingerName() {
        Song song1 = new Song("1", "Son", "path1", "Test Singer" , 180);
        Song song2 = new Song("2", "Son3", "path2","Another Singer",  200);

        when(songsRepository.getAll()).thenReturn(Set.of(song1, song2));

        Set<Song> result = command.execute();

        assertEquals(1, result.size());
        assertEquals(Set.of(song1), result);
    }
}