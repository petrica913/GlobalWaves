package app.audio.Collections;

import java.util.ArrayList;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import java.util.List;

import app.user.Artist;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Album extends AudioCollection {
    private final List<Song> songs;
    private final int releaseYear;
    private final String description;
    private Integer followers;
    @Getter
    @Setter
    private Integer order;
    @Getter
    private Artist artist;

    public Album(final String name, final String owner) {
        this(name, owner, 0, "", null, 0, null);
    }
    public Album(final String name, final String owner,
                 final int releaseYear, final String description, final ArrayList<Song> songs,
                 final Integer order, final Artist artist) {
        super(name, owner, "album");
        this.songs = songs;
        this.releaseYear = releaseYear;
        this.description = description;
        this.followers = 0;
        this.order = 0;
        this.artist = artist;
    }

    /**
     * @return the numbers of tracks of an album
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    /**
     * @param index for the track index
     * @return the track with the given index from the album
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        if (index >= 0 && index < songs.size()) {
            return songs.get(index);
        }
        return null;
    }

    /**
     * @return the number of followers of an album
     */
    public Integer getFollowers() {
        followers = 0;
        for (Song song : songs) {
            followers += song.getLikes();
        }
        return followers;
    }
}
