package bg.sofia.uni.fmi.mjt.spotify.server.business.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.business.repositories.PlaylistRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.business.repositories.SongsRepository;
import bg.sofia.uni.fmi.mjt.spotify.server.models.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.server.models.Song;

import java.util.HashMap;
import java.util.Map;

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
        Song foundSong = this.songsRepository.getByName(songName).orElseThrow();
        Playlist playlist = this.playlistRepository.getByName(playlistName).orElseThrow();
        playlist.songs().add(foundSong);
        playlistRepository.update(playlist);
        return new HashMap<>();
    }
}
