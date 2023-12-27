package app.audio.Files;

import lombok.Getter;

@Getter
public final class Episode extends AudioFile {
    private final String description;

    public Episode() {
        super("", 0, "episode");
        this.description = new String();
    }
    public Episode(final String name, final Integer duration, final String description) {
        super(name, duration, "episode");
        this.description = description;
    }
}
