package app.user;

import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Advertisement {
    @Getter
    @Setter
    private Integer timestamp;
    @Setter
    @Getter
    private ArrayList<Song> songsBetween;
    @Setter
    @Getter
    private Song ad;
    @Getter
    @Setter
    private boolean beenPlayed;
    public Advertisement() {
        this.songsBetween = new ArrayList<>();
        this.beenPlayed = false;
        this.ad = new Song();
    }
}
