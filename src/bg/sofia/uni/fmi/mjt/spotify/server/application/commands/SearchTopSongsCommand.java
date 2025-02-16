package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.application.ByListeningDescendingComparator;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.SongsRepository;

public class SearchTopSongsCommand implements Command {
    private final SongsRepository songsRepository;
    private final int songsCount;

    public SearchTopSongsCommand(SongsRepository songsRepository, int songsCount) {
        this.songsRepository = songsRepository;
        this.songsCount = songsCount;
    }

    @Override
    public Object execute() {
        return songsRepository.getAll().stream()
            .sorted(new ByListeningDescendingComparator())
            .limit(songsCount)
            .toList();
    }
}
