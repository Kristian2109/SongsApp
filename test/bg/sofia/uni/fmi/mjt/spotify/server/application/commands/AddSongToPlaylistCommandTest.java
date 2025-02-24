package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddSongToPlaylistCommandTest {
    private SongsRepository songsRepository;
    private PlaylistRepository playlistRepository;
    private AddSongToPlaylistCommand command;

    String songName = "Test Song";
    String playlistName = "My Playlist";
    private final Song mockSong = new Song("songId", songName, "Test Artist", "singer", 180);
    Playlist mockPlaylist = new Playlist("playlistId", playlistName, new HashSet<>());

    @BeforeEach
    void setUp() {
        songsRepository = mock(SongsRepository.class);
        playlistRepository = mock(PlaylistRepository.class);
    }

    @Test
    void testExecuteAddsSongToPlaylistSuccessfully() {

        when(songsRepository.getByName(songName)).thenReturn(Optional.of(mockSong));
        when(playlistRepository.getByName(playlistName)).thenReturn(Optional.of(mockPlaylist));
        when(playlistRepository.updateOrThrow(mockPlaylist)).thenReturn(mockPlaylist);

        command = new AddSongToPlaylistCommand(songsRepository, playlistRepository, songName, playlistName);

        assertDoesNotThrow(() -> command.execute());
        assertTrue(mockPlaylist.getSongs().contains(mockSong));
        verify(playlistRepository).updateOrThrow(mockPlaylist);
    }

    @Test
    void testExecuteThrowsWhenSongNotFound() {
        when(songsRepository.getByName("Nonexistent Song")).thenReturn(Optional.empty());
        command = new AddSongToPlaylistCommand(songsRepository, playlistRepository, "Nonexistent Song", "My Playlist");

        assertThrows(IllegalArgumentException.class, () -> command.execute());
    }

    @Test
    void testExecuteThrowsWhenPlaylistNotFound() {
        String songName = "Test Song";
        when(songsRepository.getByName(songName)).thenReturn(Optional.of(mockSong));
        when(playlistRepository.getByName("Nonexistent Playlist")).thenReturn(Optional.empty());

        command = new AddSongToPlaylistCommand(songsRepository, playlistRepository, songName, "Nonexistent Playlist");

        assertThrows(IllegalArgumentException.class, () -> command.execute());
    }
}