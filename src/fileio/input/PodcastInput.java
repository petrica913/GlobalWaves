package fileio.input;

import java.util.ArrayList;

public final class PodcastInput {
    @lombok.Getter
    private String name;
    @lombok.Getter
    private String owner;
    @lombok.Getter
    private ArrayList<EpisodeInput> episodes;

    public PodcastInput() {
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "PodcastInput{"
                + "name='" + name + '\''
                + ", owner='" + owner + '\''
                + ", episodes=" + episodes
                + '}';
    }
}
