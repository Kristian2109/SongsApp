package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddSongToPlaylistCommand implements Command {
    private final String songName;
    private final String playlistName;
    private final SongsRepository songsRepository;
    private final PlaylistRepository playlistRepository;

    public AddSongToPlaylistCommand(SongsRepository songsRepository, PlaylistRepository playlistRepository,
                                    String songName, String playlistName) {
        this.playlistName = playlistName;
        this.songName = songName;
        this.songsRepository = songsRepository;
        this.playlistRepository = playlistRepository;
    }

    public Map<String, Object> execute() {
        Song foundSong = this.songsRepository.getByName(songName)
            .orElseThrow(() -> new IllegalArgumentException("Invalid song name"));

        Playlist playlist = this.playlistRepository.getByName(playlistName)
            .orElseThrow(() -> new IllegalArgumentException("Invalid playlist name"));

        Set<Song> playlistSongs = playlist.getSongs();

        if (playlistSongs.contains(foundSong)) {
            throw new IllegalArgumentException("Songs is already part of this playlist");
        }

        playlistSongs.add(foundSong);
        playlistRepository.updateOrThrow(playlist);
        return new HashMap<>();
    }
}
