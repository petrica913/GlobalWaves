package app.user;

import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Advertisement {
    @Getter
    @Setter
    private Integer timestamp;
    @Setter
    @Getter
    private List<Song> songsBetween;
    public Advertisement(final Integer timestamp) {
        this.timestamp = timestamp;
        this.songsBetween = null;
    }
}
