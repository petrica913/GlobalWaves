package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Playlist extends AudioCollection {
    private final ArrayList<Song> songs;
    private Enums.Visibility visibility;
    private Integer followers;
    private int timestamp;
    @Getter
    private int seed;

    public Playlist(final String name, final String owner) {
        this(name, owner, 0);
    }

    public Playlist(final String name, final String owner, final int timestamp) {
        super(name, owner, "playlist");
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.timestamp = timestamp;
    }

    /**
     * @param song for verifying if a playlist contains a given song
     * @return a boolean value
     */
    public boolean containsSong(final Song song) {
        return songs.contains(song);
    }

    /**
     * @param song for adding a new song
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * @param song for removing a given song
     */
    public void removeSong(final Song song) {
        songs.remove(song);
    }

    /**
     * @param index for removing a song with a given index
     */
    public void removeSong(final int index) {
        songs.remove(index);
    }

    /**
     * switch the visibility of a playlist
     */
    public void switchVisibility() {
        if (visibility == Enums.Visibility.PUBLIC) {
            visibility = Enums.Visibility.PRIVATE;
        } else {
            visibility = Enums.Visibility.PUBLIC;
        }
    }

    /**
     * increases the number of followers of a playlist
     */
    public void increaseFollowers() {
        followers++;
    }

    /**
     * decreases the number of followers of a playlist
     */
    public void decreaseFollowers() {
        followers--;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean isVisibleToUser(final String user) {
        return this.getVisibility() == Enums.Visibility.PUBLIC
                || (this.getVisibility() == Enums.Visibility.PRIVATE
                        && this.getOwner().equals(user));
    }

    @Override
    public boolean matchesFollowers(final String followerNum) {
        return filterByFollowersCount(this.getFollowers(), followerNum);
    }

    /**
     * Filters a plylist by the number of the followers
     * @param count for counting
     * @param query for query
     * @return
     */
    private static boolean filterByFollowersCount(final int count, final String query) {
        if (query.startsWith("<")) {
            return count < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return count > Integer.parseInt(query.substring(1));
        } else {
            return count == Integer.parseInt(query);
        }
    }

    /**
     * For returning the number of likes of a playlist
     * @return total likes
     */
    public int getTotalLikes() {
        int totalLikes = 0;
        for (Song song : this.songs) {
            totalLikes += song.getLikes();
        }
        return totalLikes;
    }

    public void setSeed(final int seed) {
        this.seed = seed;
    }

    /**
     * Removes a list of given songs from the playlist's songs
     * @param songsToRemove for the list of songs to be removed
     */
    public void removeSongs(final List<Song> songsToRemove) {
        songs.removeAll(songsToRemove);
    }

}
