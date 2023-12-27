package app.user.pages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import app.audio.LibraryEntry;
import app.user.User;
import app.audio.Collections.Playlist;

public class HomePage implements Page {
    private final User user;
    private static final int MAGIC_NUMBER = 5;

    public HomePage(final User user) {
        this.user = user;
    }

    /**
     * Method to display a page
     * @return the page content
     */
    @Override
    public String display() {
        return "Liked songs:\n\t" + displayRecommendedSongs()
                + "\n\n" + "Followed playlists:\n\t"
                + displayTopFollowedPlaylists();
    }

    /**
     * @return the type of page
     */
    @Override
    public String type() {
        return "Home";
    }

    /**
     * Display recommended songs on the home page
     * @return songs
     */
    private String displayRecommendedSongs() {
        List<String> recommendedSongs = user.getLikedSongs().stream()
                .sorted(Comparator.comparingInt(song -> -song.getLikes())).limit(MAGIC_NUMBER)
                .map(LibraryEntry::getName)
                .collect(Collectors.toList());

        return recommendedSongs.toString();
    }

    /**
     * Display the recommended playlists
     * @return the playlists
     */
    private String displayTopFollowedPlaylists() {
        List<Playlist> topFollowedPlaylists = user.getFollowedPlaylists().stream()
                .sorted(Comparator.comparingInt(Playlist::getTotalLikes).reversed())
                .limit(MAGIC_NUMBER)
                .collect(Collectors.toList());
        List<String> followedPlaylists = new ArrayList<>();
        for (Playlist playlist : topFollowedPlaylists) {
            followedPlaylists.add(playlist.getName());
        }

        return followedPlaylists.toString();
    }
}
