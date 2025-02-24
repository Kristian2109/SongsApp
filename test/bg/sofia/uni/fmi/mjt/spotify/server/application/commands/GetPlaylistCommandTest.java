package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetPlaylistCommandTest {
    private PlaylistRepository playlistRepository;
    private GetPlaylistCommand command;
    private final String playlistName = "My Playlist";

    @BeforeEach
    void setUp() {
        playlistRepository = mock(PlaylistRepository.class);
        command = new GetPlaylistCommand(playlistRepository, playlistName);
    }

    @Test
    void testExecuteReturnsPlaylistWhenPlaylistExists() {
        Playlist mockPlaylist = new Playlist("1", playlistName, new HashSet<>());
        when(playlistRepository.getByName(playlistName)).thenReturn(Optional.of(mockPlaylist));

        Object result = command.execute();

        assertEquals(mockPlaylist, result, "The returned playlist should match the expected playlist.");
        verify(playlistRepository).getByName(playlistName);
    }

    @Test
    void testExecuteThrowsExceptionWhenPlaylistDoesNotExist() {
        when(playlistRepository.getByName(playlistName)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> command.execute(), "The command should throw an exception when the playlist does not exist.");
        verify(playlistRepository).getByName(playlistName);
    }
}