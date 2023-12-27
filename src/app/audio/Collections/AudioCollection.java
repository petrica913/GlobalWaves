package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;
    private final String type;

    public AudioCollection(final String name, final String owner, String type) {
        super(name);
        this.owner = owner;
        this.type = type;
    }

    /**
     * Returns the number of tracks from a given audio collection
     * @return number of tracks
     */
    public abstract int getNumberOfTracks();

    /**
     * Returns a track with a given index
     * @param index for index
     * @return the track with the given index
     */
    public abstract AudioFile getTrackByIndex(int index);

    /**
     * Verifies the match between owner and user
     * @param user for verifying if the owner matches with the user
     * @return
     */
    @Override
    public boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }
}
