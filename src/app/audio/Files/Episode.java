package app.audio.Files;

import lombok.Getter;

@Getter
public final class Episode extends AudioFile {
    private final String description;
    private final String owner;

    public Episode() {
        super("", 0, "episode");
        this.description = new String();
        this.owner = new String();
    }
    public Episode(final String name, final Integer duration,
                   final String description, final String owner) {
        super(name, duration, "episode");
        this.description = description;
        this.owner = owner;
    }
}
