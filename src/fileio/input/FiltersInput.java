package fileio.input;

import java.util.ArrayList;

public final class FiltersInput {
    @lombok.Getter
    private String name;
    @lombok.Getter
    private String album;
    @lombok.Getter
    private ArrayList<String> tags;
    @lombok.Getter
    private String lyrics;
    @lombok.Getter
    private String genre;
    @lombok.Getter
    private String releaseYear; // pentru search song/episode -> releaseYear
    @lombok.Getter
    private String artist;
    @lombok.Getter
    private String owner; // pentru search playlist si podcast
    @lombok.Getter
    private String followers; // pentru search playlist -> followers

    public FiltersInput() {
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setArtist(final String artist) {
        this.artist = artist;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setFollowers(final String followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "FilterInput{"
                + ", name='" + name + '\''
                + ", album='" + album + '\''
                + ", tags=" + tags
                + ", lyrics='" + lyrics + '\''
                + ", genre='" + genre + '\''
                + ", releaseYear='" + releaseYear + '\''
                + ", artist='" + artist + '\''
                + ", owner='" + owner + '\''
                + ", followers='" + followers + '\''
                + '}';
    }
}
