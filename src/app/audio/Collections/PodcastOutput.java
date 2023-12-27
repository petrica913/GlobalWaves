package app.audio.Collections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PodcastOutput {
    @lombok.Getter
    private final String name;
    @lombok.Getter
    private final ArrayList<String> episodes;

    /**
     *
     * @param podcast for transforming podcast in PodcastOutput
     */
    public PodcastOutput(final Podcast podcast) {
        this.name = podcast.getName();
        this.episodes = new ArrayList<>();
        for (int i = 0; i < podcast.getEpisodes().size(); i++) {
            episodes.add(podcast.getEpisodes().get(i).getName());
        }
    }

}
