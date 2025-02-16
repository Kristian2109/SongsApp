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

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getListeningsCount() {
        return listeningsCount;
    }

    public void incrementListings() {
        this.listeningsCount++;
    }
}
