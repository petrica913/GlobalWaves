package app.audio;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public abstract class LibraryEntry {
    private final String name;

    /**
     * Instantiates a new Library entry.
     *
     * @param name the name
     */
    public LibraryEntry(final String name) {
        this.name = name;
    }

    /**
     * @param name for verifying if the names match
     * @return
     */

    public boolean matchesName(final String name) {
        return getName().toLowerCase().startsWith(name.toLowerCase());
    }

    /**
     * @param album for verifying if the albums match
     * @return
     */
    public boolean matchesAlbum(final String album) {
        return false;
    }

    /**
     * @param tags for verifying if the tags match
     * @return
     */
    public boolean matchesTags(final ArrayList<String> tags) {
        return false;
    }

    /**
     * @param lyrics for verifying if the lyrics match
     * @return
     */
    public boolean matchesLyrics(final String lyrics) {
        return false;
    }

    /**
     *
     * @param genre for verifying if the genres match
     * @return
     */
    public boolean matchesGenre(final String genre) {
        return false;
    }

    /**
     *
     * @param artist for verifying if the artists match
     * @return
     */
    public boolean matchesArtist(final String artist) {
        return false;
    }

    /**
     *
     * @param releaseYear for verifying if the years of
     *                   release match
     * @return
     */
    public boolean matchesReleaseYear(final String releaseYear) {
        return false;
    }

    /**
     *
     * @param user for verifying if the owner matches with the user
     * @return
     */
    public boolean matchesOwner(final String user) {
        return false;
    }

    /**
     *
     * @param user for verifying if the library entry is visible to the user
     * @return
     */
    public boolean isVisibleToUser(final String user) {
        return false;
    }

    /**
     *
     * @param followers for verifying if the followers match
     * @return
     */
    public boolean matchesFollowers(final String followers) {
        return false;
    }
}
