package app.statistics;

import app.user.Artist;
public class ArtistStatistics implements Statistics{
    private final Artist artist;
    public ArtistStatistics(final Artist artist) {
        this.artist = artist;
    }
    @Override
    public String generateStatistics() {
//        implement stats for artist
        return null;
    }
}
