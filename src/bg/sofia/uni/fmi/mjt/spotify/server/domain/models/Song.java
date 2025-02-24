package bg.sofia.uni.fmi.mjt.spotify.server.domain.models;

public class Song extends Identifiable {
    private String name;
    private String source;
    private String singerName;
    private int listeningsCount;

    public Song(String id, String name, String source, String singerName, int listeningsCount) {
        super(id);
        this.name = name;
        this.source = source;
        this.singerName = singerName;
        this.listeningsCount = listeningsCount;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getSingerName() {
        return singerName;
    }

    public int getListeningsCount() {
        return listeningsCount;
    }

    public void incrementListings() {
        this.listeningsCount++;
    }
}
