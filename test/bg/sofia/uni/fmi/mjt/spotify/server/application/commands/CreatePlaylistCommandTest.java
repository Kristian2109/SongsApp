package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreatePlaylistCommandTest {
    private PlaylistRepository playlistRepository;
    private CreatePlaylistCommand command;
    private final String playlistName = "My New Playlist";

    @BeforeEach
    void setUp() {
        playlistRepository = mock(PlaylistRepository.class);
        command = new CreatePlaylistCommand(playlistRepository, playlistName);
    }

    @Test
    void testExecuteCreatesPlaylistSuccessfully() {
        Playlist mockPlaylist = new Playlist("id", playlistName, new HashSet<>());
        when(playlistRepository.create(any(Playlist.class))).thenReturn(mockPlaylist);

        Object result = command.execute();

        assertEquals(mockPlaylist, result, "The returned playlist should match the created playlist.");
        verify(playlistRepository).create(argThat(playlist -> playlistName.equals(playlist.getName())));
    }
}