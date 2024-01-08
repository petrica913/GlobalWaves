package app.user;

import java.util.ArrayList;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.user.Collections.Event;
import app.user.Collections.Merch;

import lombok.Getter;
import lombok.Setter;

public class Artist extends User {
    @Getter
    private ArrayList<Album> albums;
    @Getter
    private ArrayList<Event> events;
    @Getter
    private ArrayList<Merch>  merches;
    @Getter
    private Integer likes;
    @Getter
    private double songRevenue;
    @Getter
    private double merchRevenue;
    @Getter
    @Setter
    private Integer ranking;
    @Getter
    private String mostProfitableSong;
    @Setter
    @Getter
    private boolean play;
    @Setter
    @Getter
    private boolean boughtMerch;
    @Setter
    @Getter
    private Integer order;

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merches = new ArrayList<>();
        likes = 0;
        songRevenue = 0;
        merchRevenue = 0;
        ranking = 1;
        mostProfitableSong = "N/A";
    }

    /**
     * Adds an album
     * @param album for album
     * @return the message of the result
     */
    public String addAlbum(final Album album) {
        albums.add(album);
        Admin.getInstance().addSongs(album.getSongs());
        return getUsername() + " has added new album successfully.";
    }

    /**
     * Shows the albums of an artist
     * @return the message
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> result = new ArrayList<>();
        for (Album album : albums) {
            result.add(new AlbumOutput(album));
        }
        return result;
    }

    /**
     * Adds an event
     * @param event for event
     * @return the message
     */
    public String addEvent(final Event event) {
        events.add(event);
        return getUsername() + " has added new event successfully.";
    }

    /**
     * Removes an event
     * @param eventName for the name of the event
     * @return the message
     */
    public String removeEvent(final String eventName) {
        Event eventToRemove = null;
        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                eventToRemove = event;
            }
        }
        if (eventToRemove == null) {
            return getUsername() + " doesn't have an event with the given name.";
        }
        events.remove(eventToRemove);
        return getUsername() + " deleted the event successfully.";
    }

    /**
     * Adds merch for an artist
     * @param merch for merch
     * @return the result
     */
    public String addMerch(final Merch merch) {
        merches.add(merch);
        return getUsername() + " has added new merchandise successfully.";
    }

    /**
     * Removes an album
     * @param albumName for the name of the album
     * @return the result
     */
    public String removeAlbum(final String albumName) {
        Album albumToRemove = null;
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                albumToRemove = album;
                break;
            }
        }
        if (albumToRemove == null) {
            return getUsername() + " doesn't have an album with the given name.";
        }
        boolean variable = canAlbumBeDeleted(albumToRemove);
        if (!variable) {
            return getUsername() + " can't delete this album.";
        }
        for (User user : Admin.getInstance().getUsers()) {
            for (Playlist playlist : user.getPlaylists()) {
                Integer size = playlist.getSongs().size();
                playlist.removeSongs(albumToRemove.getSongs());
            }
            ArrayList<Song> likedSongs = user.getLikedSongs();
            likedSongs.removeAll(albumToRemove.getSongs());
            user.setLikedSongs(likedSongs);
        }
        albums.remove(albumToRemove);
        return getUsername() + " deleted the album successfully.";
    }

    /**
     * Verifies if an album can be removed
     * @param albumToRemove the album to be removed
     * @return a boolean
     */
    private boolean canAlbumBeDeleted(final Album albumToRemove) {
        for (User user : Admin.getInstance().getUsers()) {
            AudioFile audioFile = user.getPlayer().getCurrentAudioFile();
            Song song = null;
            if (audioFile != null) {
                if (audioFile.getType().equals("song")) {
                    song = (Song) audioFile;
                }
            }
            if (audioFile == null || song == null) {
                continue;
            } else if (song.getAlbum().equals(albumToRemove.getName())) {
                return false;
            }
            AudioCollection audioCollection = user.getPlayer().getSource().getAudioCollection();
            if (audioCollection != null) {
                if (audioCollection.getType().equals("playlist")) {
                    Playlist playlist = (Playlist) audioCollection;
                    for (Song song1 : playlist.getSongs()) {
                        if (albumToRemove.getSongs().contains(song1)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets the likes an artist gets
     * @param likes for likes
     */
    public void setLikes(final Integer likes) {
        this.likes = likes;
    }
    public void updateSongRevenue(Integer totalSongs, Integer artistSongs) {
        this.songRevenue = this.songRevenue + (double) (1000000 / totalSongs * artistSongs);
//        this.songRevenue = Math.round(this.songRevenue * 100.0) / 100.0;
    }
}
