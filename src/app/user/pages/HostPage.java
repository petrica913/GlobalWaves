package app.user.pages;

import app.Admin;
import app.audio.Collections.Podcast;
import app.user.Collections.Announcement;
import app.user.Host;
import app.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class HostPage implements Page {
    @lombok.Getter
    private final User user;
    private final Host host;

    public HostPage(final User user) {
        this.user = user;
        this.host = (Host) Admin.getUser(user.getUsername());
    }

    /**
     * @return the content of the host page
     */
    @Override
    public String display() {
        return "Podcasts:\n\t" + displayPodcasts()
                + "\n\n" + "Announcements:\n\t" + displayAnnouncements();
    }

    /**
     * @return page type
     */
    @Override
    public String type() {
        return "HostPage";
    }

    /**
     * Display the podcasts of a host
     * @return the result
     */
    private String displayPodcasts() {
        StringBuilder result = new StringBuilder("[");

        int i = 0;
        for (Podcast podcast : host.getPodcasts()) {
            result.append(podcast.getName()).append(":\n\t");

            List<String> episodeDetails = podcast.getEpisodes().stream()
                    .map(episode -> String.format("%s - %s, ", episode.getName(),
                            episode.getDescription())).collect(Collectors.toList());

            if (!episodeDetails.isEmpty()) {
                episodeDetails.set(episodeDetails.size() - 1,
                        episodeDetails.get(episodeDetails.size() - 1)
                                .replace(", ", ""));
            }

            result.append("[").append(String.join("", episodeDetails)).append("]");

            if (i < host.getPodcasts().size() - 1) {
                result.append("\n, ");
            }
            if (i == host.getPodcasts().size() - 1) {
                result.append("\n]");
            }
            i++;
        }

        return result.toString();
    }


    private String displayAnnouncements() {
        StringBuilder result = new StringBuilder("[");

        int i = 0;
        for (Announcement announcement : host.getAnnouncements()) {
            result.append(announcement.getName()).append(":\n\t")
                    .append(announcement.getDescription());

            if (i < host.getAnnouncements().size() - 1) {
                result.append("\n");
            }
            if (i == host.getAnnouncements().size() - 1) {
                result.append("\n");
            }
            i++;
        }
        result.append("]");
        return result.toString();
    }


}
