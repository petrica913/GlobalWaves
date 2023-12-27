package fileio.input;

public final class EpisodeInput {
    @lombok.Getter
    private String name;
    @lombok.Getter
    private Integer duration;
    @lombok.Getter
    private String description;

    public EpisodeInput() {
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "EpisodeInput{"
                + "name='" + name + '\''
                + ", description='" + description + '\''
                + ", duration=" + duration
                + '}';
    }
}
