package app.user;

import java.util.ArrayList;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.AudioFile;
import app.player.PodcastBookmark;
import app.user.Collections.Announcement;
import lombok.Getter;

public final class Host extends User {
    @Getter
    private final ArrayList<Podcast> podcasts;
    @Getter
    private final ArrayList<Announcement> announcements;

    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }

    /**
     * Adds podcast
     * @param podcast for podcast
     * @return the result message
     */
    public String addPodcast(final Podcast podcast) {
        if (podcasts.contains(podcast)) {
            return "Podcast already added.";
        }
        podcasts.add(podcast);
        return getUsername() + " has added new podcast successfully.";
    }

    /**
     * Adds announcement
     * @param announcement for announcement
     * @return the result message
     */
    public String addAnnouncement(final Announcement announcement) {
        announcements.add(announcement);
        return getUsername() + " has successfully added new announcement.";
    }

    /**
     * Removes an announcement
     * @param announcementToRemove for the announcement
     */
    public void removeAnnouncement(final Announcement announcementToRemove) {
        for (Announcement announcement : announcements) {
            if (announcement.equals(announcementToRemove)) {
                announcements.remove(announcementToRemove);
                break;
            }
        }
    }

    /**
     * Show podcasts
     * @return result
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> result = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            result.add(new PodcastOutput(podcast));
        }
        return result;
    }

    /**
     * Removes a podcast
     * @param podcastName for name
     * @return result
     */
    public String removePodcast(final String podcastName) {
        Podcast podcastToRemove = null;
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(podcastName)) {
                podcastToRemove = podcast;
                break;
            }
        }
        if (podcastToRemove == null) {
            return getUsername() + " doesn't have a podcast with the given name.";
        }
        if (!canPodcastBeDeleted(podcastToRemove)) {
            return getUsername() + " can't delete this podcast.";
        }
        podcasts.remove(podcastToRemove);

        return getUsername() + " deleted the podcast successfully.";
    }

    /**
     * Verifies if the podcast can be deleted
     * @param podcast for the podcast
     * @return result
     */
    private boolean canPodcastBeDeleted(final Podcast podcast) {
        for (User user : Admin.getInstance().getUsers()) {
            AudioFile audioFile = user.getPlayer().getCurrentAudioFile();
            if (audioFile == null) {
                continue;
            } else if (user.getPlayer().getSource().getAudioCollection()
                    .getName().equals(podcast.getName())) {
                return false;
            } else if (user.getPlayer().getBookmarks() != null) {
                for (PodcastBookmark podcastBookmark : user.getPlayer().getBookmarks()) {
                    if (podcast.getName().equals(podcastBookmark.getName())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
