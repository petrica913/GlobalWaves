package app.audio.Files;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;
    private double revenue;

    public Song() {
        super("", 0, "song"); // Call to the superclass default constructor
        this.album = "";
        this.tags = new ArrayList<>();
        this.lyrics = "";
        this.genre = "";
        this.releaseYear = 0;
        this.artist = "";
        this.likes = 0;
        this.revenue = 0;
    }

    public Song(final String name, final Integer duration, final String album,
                final ArrayList<String> tags, final String lyrics, final String genre,
                final Integer releaseYear, final String artist) {
        super(name, duration, "song");
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
    }

    /**
     * Verifies the match between albums
     * @param albumNum for verifying if the albums match
     * @return
     */
    @Override
    public boolean matchesAlbum(final String albumNum) {
        return this.getAlbum().equalsIgnoreCase(albumNum);
    }

    /**
     * Verifies the match between tags
     * @param tagsNum for verifying if the tags match
     * @return
     */
    @Override
    public boolean matchesTags(final ArrayList<String> tagsNum) {
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : tagsNum) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies the match between lyrics
     * @param lyricsNum for verifying if the lyrics match
     * @return
     */
    @Override
    public boolean matchesLyrics(final String lyricsNum) {
        return this.getLyrics().toLowerCase().contains(lyricsNum.toLowerCase());
    }

    /**
     * Verifies the match between the genres
     * @param genreNum for verifying if the genres match
     * @return
     */
    @Override
    public boolean matchesGenre(final String genreNum) {
        return this.getGenre().equalsIgnoreCase(genreNum);
    }

    /**
     * Verifies the match between artists
     * @param artistNum for verifying if the artists match
     * @return
     */
    @Override
    public boolean matchesArtist(final String artistNum) {
        return this.getArtist().equalsIgnoreCase(artistNum);
    }

    /**
     * @param releaseYearNum for verifying if the years of
     *                   release match
     * @return
     */
    @Override
    public boolean matchesReleaseYear(final String releaseYearNum) {
        return filterByYear(this.getReleaseYear(), releaseYearNum);
    }

    /**
     * Verifies if the songs are filtered by year
     * @param year for year
     * @param query for query
     * @return a boolean
     */
    private static boolean filterByYear(final int year, final String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }

    /**
     * For giving a like to a song
     */
    public void like() {
        likes++;
    }

    /**
     * For disliking a song
     */
    public void dislike() {
        likes--;
    }
    public void updateRevenue(double newRevenue) {
        this.revenue += newRevenue;
    }
    public void resetRevenue() {
        this.revenue = 0;
    }
}
