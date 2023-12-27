package app.audio.Collections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumOutput {
    @lombok.Getter
    private final String name;
    @lombok.Getter
    private final ArrayList<String> songs;

    /**
     * Transforms an album to AlbumOutput type
     * @param album for the album
     */
    public AlbumOutput(final Album album) {
        this.name = album.getName();
        this.songs = new ArrayList<>();
        for (int i = 0; i < album.getSongs().size(); i++) {
            songs.add(album.getSongs().get(i).getName());
        }
    }

}
