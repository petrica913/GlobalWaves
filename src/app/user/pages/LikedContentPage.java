package app.user.pages;

import app.user.User;
import java.util.List;
import java.util.stream.Collectors;

public class LikedContentPage implements Page {
    private final User user;

    public LikedContentPage(final User user) {
        this.user = user;
    }

    /**
     * @return the page content
     */
    @Override
    public String display() {
        return displayLikedSongs() + displayFollowedPlaylists();
    }

    /**
     * @return the page type
     */
    @Override
    public String type() {
        return "LikedContent";
    }

    /**
     * @return the liked songs by a user
     */
    private String displayLikedSongs() {
        StringBuilder likedSongsResult = new StringBuilder("Liked songs:\n\t[");
        List<String> likedSongs = user.getLikedSongs().stream()
                .map(song -> song.getName() + " - " + song.getArtist())
                .collect(Collectors.toList());

        likedSongsResult.append(String.join(", ", likedSongs)).append("]\n\n");
        return likedSongsResult.toString();
    }

    /**
     * @return the followed playlists by a user
     */
    private String displayFollowedPlaylists() {
        StringBuilder followedPlaylistsResult = new StringBuilder("Followed playlists:\n\t[");
        List<String> followedPlaylists = user.getFollowedPlaylists().stream()
                .map(playlist -> playlist.getName() + " - " + playlist.getOwner())
                .collect(Collectors.toList());

        followedPlaylistsResult.append(String.join(", ", followedPlaylists)).append("]");
        return followedPlaylistsResult.toString();
    }

    @Override
    public void accept(PageVisitor visitor) {
        visitor.visit(this);
    }
}
