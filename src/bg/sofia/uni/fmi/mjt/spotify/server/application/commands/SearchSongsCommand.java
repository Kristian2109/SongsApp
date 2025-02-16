package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

import java.util.Set;

public class SearchSongsCommand implements Command {
    private final SongsRepository songsRepository;
    private final Set<String> keywords;

    public SearchSongsCommand(SongsRepository songsRepository, Set<String> keywords) {
        this.songsRepository = songsRepository;
        this.keywords = keywords;
    }

    @Override
    public Object execute() {
        Set<Song> allSongs = songsRepository.getAll();
        return allSongs.stream().filter(this::containsKeyword).toList();
    }

    private boolean containsKeyword(Song song) {
        return keywords.stream().anyMatch(keyword ->
            song.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                song.getSingerName().toLowerCase().contains(keyword.toLowerCase())
        );
    }
}
