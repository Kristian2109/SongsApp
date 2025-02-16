package bg.sofia.uni.fmi.mjt.spotify.server.application;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Song;

import java.util.Comparator;

public class ByListeningDescendingComparator implements Comparator<Song> {
    @Override
    public int compare(Song o1, Song o2) {
        return o2.getListeningsCount() - o1.getListeningsCount();
    }
}
