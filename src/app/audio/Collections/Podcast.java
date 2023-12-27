package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import fileio.input.EpisodeInput;

import java.util.ArrayList;
import java.util.List;

public final class Podcast extends AudioCollection {
    private final List<Episode> episodes;

    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        super(name, owner, "podcast");
        this.episodes = episodes;
    }

    /**
     *
     * @return
     */
    public List<Episode> getEpisodes() {
        return episodes;
    }

    /**
     *
     * @return
     */
    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    /**
     *
     * @param index
     * @return
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return episodes.get(index);
    }

    /**
     *
     * @return
     */
    public ArrayList<EpisodeInput> getEpisodesInput() {
        ArrayList<EpisodeInput> newEpisodesList = new ArrayList<>();
        for (Episode episode : episodes) {
            EpisodeInput episodeInput = new EpisodeInput();
            episodeInput.setName(episode.getName());
            episodeInput.setDescription(episode.getDescription());
            episodeInput.setDuration(episode.getDuration());
            newEpisodesList.add(episodeInput);
        }
        return newEpisodesList;
    }
}
