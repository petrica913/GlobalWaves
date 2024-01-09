package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Collections.Album;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.user.Collections.Announcement;
import app.user.pages.Page;
import app.user.pages.ArtistPage;
import app.user.pages.HomePage;
import app.user.pages.HostPage;
import app.user.pages.LikedContentPage;
import app.user.Collections.Event;
import app.user.Collections.Merch;
import app.utils.Enums;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class User {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    private boolean online;
    @Getter
    private String type;
    @Getter
    private HomePage homePage;
    @Getter
    private LikedContentPage likedContentPage;
    @Getter
    private Page nextPage;
    @Setter
    @Getter
    private ArrayList<Song> topSongs;
    @Setter
    @Getter
    private ArrayList<Episode> topEpisodes;
    @Setter
    @Getter
    private Integer listeners;
    @Setter
    @Getter
    private Integer credits;
    @Getter
    @Setter
    private Advertisement advertisement;
    @Setter
    @Getter
    private ArrayList<Song> premiumSongs;

    public User(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        online = true;
        this.nextPage = new HomePage(this);
        topSongs = new ArrayList<>();
        listeners = 0;
        credits = 0;
        premiumSongs = new ArrayList<>();
        player.setOwner(this);
        advertisement = new Advertisement();
    }

    /**
     * For search command
     * @param filters for filters
     * @param type for type
     * @return the result message
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        if (!online) {
            return results;
        }
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }
        return results;
    }

    /**
     * For the select command
     * @param itemNumber for the item number
     * @return the command message result
     */
    public String select(final int itemNumber) {
        if (!online) {
            return username + " is offline.";
        }
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }
        if (searchBar.getLastSearchType().equals("artist")) {
            nextPage = new ArtistPage(Admin.getUser(selected.getName()));
            return "Successfully selected %s's page.".formatted(selected.getName());
        }
        if (searchBar.getLastSearchType().equals("host")) {
            nextPage = new HostPage(Admin.getUser(selected.getName()));
            return "Successfully selected %s's page.".formatted(selected.getName());
        }

        return "Successfully selected %s.".formatted(selected.getName());
    }

    /**
     * For the load command
     * @return the load message result
     */
    public String load() {
        if (!online) {
            return username + " is offline.";
        }
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }
        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        if (player.getSource().getAudioFile().getType().equals("song")) {
            topSongs.add((Song) player.getSource().getAudioFile());
            Integer listeners = this.getListeners();
            listeners++;
            this.setListeners(listeners);
            if (this.getCredits() != 0) {
                premiumSongs.add((Song) player.getSource().getAudioFile());
            }
            if (this.getCredits() == 0) {
                this.addSongToAd((Song) player.getSource().getAudioFile());
            }
        }
        if (player.getSource().getAudioFile().getType().equals("episode")) {
            topEpisodes.add((Episode) player.getSource().getAudioFile());
        }

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play/pause a user's player
     * @return the PlayPause result message
     */
    public String playPause() {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Puts the user's player on repeat
     * @return the result message
     */
    public String repeat() {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            default -> {
                repeatStatus = "repeat current song";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Puts the user's player on shuffle
     * @param seed for seed
     * @return the result message
     */
    public String shuffle(final Integer seed) {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * For the forward command
     * @return the result message
     */
    public String forward() {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * For the backward command
     * @return the result message
     */
    public String backward() {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Likes the selection made by the user
     * @return the result message
     */
    public String like() {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Loads the next audio file on user's player
     * @return the result message
     */
    public String next() {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * For the previous command
     * @return the result message
     */
    public String prev() {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * The user creates a new playlist
     * @param name for name
     * @param timestamp for name
     * @return the result message
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (!online) {
            return username + " is offline.";
        }
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Removes/adds the selected audio file into the audio collection
     * @param id for the id
     * @return the result message
     */
    public String addRemoveInPlaylist(final int id) {
        if (!online) {
            return username + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switches the visibility of a playlist
     * (should've been done in the playlist class)
     * @param playlistId for the playlistId
     * @return the result message
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (!online) {
            return username + " is offline.";
        }
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }
        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Shows the playlists
     * (also should've been done in the playlist class)
     * @return the result message
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * For the follow command
     * @return the result message
     */
    public String follow() {
        if (!online) {
            return username + " is offline.";
        }
        LibraryEntry selection = searchBar.getLastSelected();
        String typeNum = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!typeNum.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * @return the stats of the current user's player
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Sets the liked songs list by the user
     * @param likedSongs for likedSongs
     */
    public void setLikedSongs(final ArrayList<Song> likedSongs) {
        this.likedSongs = likedSongs;
    }

    /**
     * Shows the preferred songs of the user
     * @return the result message
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets the preferred genre of the user
     * @return the result message
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Simulates the time for the user
     * @param time for the difference between last 2 timestamps
     */
    public void simulateTime(final int time) {
        if (online) {
            player.simulatePlayer(time);
            topSongs = player.getTopSongs();
            topEpisodes = player.getTopEpisodes();
            homePage = new HomePage(this);
            likedContentPage = new LikedContentPage(this);
        }
    }

    /**
     * Switches the connection status of a user
     * @return the result message
     */
    public String switchConnectionStatus() {
        online = !online;
        return username + " has changed status successfully.";
    }

    /**
     * Sets the type of user
     * @param type for type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Sets the current page
     * @param nextPage for the given page
     */
    public void setNextPage(final Page nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * Changes the page of a user
     * @param pageName for the page name
     * @return the result message
     */
    public String changePage(final String pageName) {
        switch (pageName.toLowerCase()) {
            case "home":
                nextPage = homePage;
                break;
            case "likedcontent":
                nextPage = likedContentPage;
                break;
            default:
                return username + " is trying to access a non-existent page.";
        }
        return username + " accessed " + nextPage.type() + " successfully.";
    }

    /**
     * Prints the current page
     * @return the result message
     */
    public String printCurrentPage() {
        if (nextPage.type().equals("HomePage")) {
            nextPage = new HomePage(this);
        }
        if (nextPage.type().equals("LikedContentPage")) {
            nextPage = new LikedContentPage(this);
        }
        if (nextPage != null) {
            return nextPage.display();
        } else {
            return "No next page set.";
        }
    }

    /**
     * Adds a new album
     * @param albumName for album name
     * @param releaseYear for year of release
     * @param description for description
     * @param songs for songs
     * @return the results message
     */
    public String addAlbum(final String albumName, final int releaseYear,
                           final String description, final ArrayList<Song> songs,
                           final Integer order) {
        if (!(this.getType().equals("artist"))) {
            return username + " is not an artist.";
        }
        Artist artist = (Artist) this;
        Album newAlbum = new Album(albumName, username, releaseYear,
                description, songs, order, (Artist) this);
        if (artist.getAlbums().stream().anyMatch(album -> album.getName().equals(albumName))) {
            return username + " has another album with the same name.";
        }
        if (containsDuplicateSongs(songs)) {
            return username + " has the same song at least twice in this album.";
        }
        List<Album> newlist = Admin.getAlbums();
        newlist.add(newAlbum);
        Admin.getInstance().setAlbums(newlist);
        Integer count = Admin.getInstance().getAlbumsCount();
        count += 1;
        Admin.getInstance().setAlbumsCount(count);
        newAlbum.setOrder(count);
        return artist.addAlbum(newAlbum);
    }

    /**
     * Adds an event
     * @param eventName for the name of the event
     * @param eventDescription for the description
     * @param eventDate for the date
     * @return the result message
     */
    public String addEvent(final String eventName,
                           final String eventDescription, final String eventDate) {
        if (this.getType() == null) {
            return username + " is not an artist.";
        }
        if (!(this.getType().equals("artist"))) {
            return username + " is not an artist.";
        }

        Artist artist = (Artist) this;

        if (Admin.getUser(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (artist.getEvents().stream().anyMatch(event -> event.getName().equals(eventName))) {
            return username + " has another event with the same name.";
        }

        Event newEvent = new Event(eventName, eventDescription, eventDate);

        if (!newEvent.isValidDate()) {
            return "Event for " + username + " does not have a valid date.";
        }

        artist.addEvent(newEvent);
        return username + " has added new event successfully.";
    }

    /**
     * Adds merchandise
     * @param merchName for merch name
     * @param description for description
     * @param price for price
     * @return the result message
     */
    public String addMerchandise(final String merchName,
                                 final String description, final Integer price) {
        if (Admin.getUser(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (this.getType() == null) {
            return username + " is not an artist.";
        }
        if (!(this.getType().equals("artist"))) {
            return username + " is not an artist.";
        }

        Artist artist = (Artist) this;

        if (artist.getMerches().stream().anyMatch(merch -> merch.getName().equals(merchName))) {
            return username + " has merchandise with the same name.";
        }

        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }

        Merch newMerch = new Merch(merchName, description, price);
        artist.addMerch(newMerch);
        return username + " has added new merchandise successfully.";
    }

    /**
     * Adds a podcast
     * @param name for the name
     * @param episodes for episodes
     * @return the result message
     */
    public String addPodcast(final String name, final ArrayList<Episode> episodes) {
        if (this.getType() == null) {
            return username + " is not a host.";
        }
        if (!(this.getType().equals("host"))) {
            return username + " is not a host.";
        }
        Host host = (Host) this;
        Podcast newPodcast = new Podcast(name, username, episodes);
        if (host.getPodcasts().stream().anyMatch(podcast -> podcast.getName().equals(name))) {
            return username + " has another podcast with the same name.";
        }
        for (int i = 0; i < newPodcast.getNumberOfTracks() - 2; i++) {
            for (int j = i + 1; j < newPodcast.getNumberOfTracks() - 1; j++) {
                if (newPodcast.getEpisodes().get(i).getName()
                        .equals(newPodcast.getEpisodes().get(j).getName())) {
                    return username + " has the same episode in this podcast.";
                }
            }
        }
        List<PodcastInput> newlist = new ArrayList<>();
        for (Podcast podcast : Admin.getInstance().getPodcasts()) {
            PodcastInput podcastInput = new PodcastInput();
            podcastInput.setEpisodes(podcast.getEpisodesInput());
            podcastInput.setName(podcast.getName());
            podcastInput.setOwner(podcast.getOwner());
            newlist.add(podcastInput);
        }
        PodcastInput newpodcastInput = new PodcastInput();
        newpodcastInput.setName(newPodcast.getName());
        newpodcastInput.setOwner(newPodcast.getOwner());
        newpodcastInput.setEpisodes(newPodcast.getEpisodesInput());
        newlist.add(newpodcastInput);
        Admin.getInstance().setPodcasts(newlist);
        return host.addPodcast(newPodcast);
    }

    /**
     * Verifies if a list of songs contains duplicates
     * @param songs for songs
     * @return a boolean
     */
    private boolean containsDuplicateSongs(final ArrayList<Song> songs) {
        ArrayList<String> songNames = new ArrayList<>();
        for (int i = 0; i < songs.size() - 1; i++) {
            for (int j = i + 1; j < songs.size(); j++) {
                if (songs.get(i).getName().equals(songs.get(j).getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds an announcement
     * @param nameAnnouncement for name
     * @param description for description
     * @return the result message
     */
    public String addAnnouncement(final String nameAnnouncement, final String description) {
        if (this.getType() == null) {
            return username + " is not a host.";
        }
        if (!(this.getType().equals("host"))) {
            return username + " is not a host.";
        }

        Host host = (Host) this;

        if (Admin.getUser(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        if (host.getAnnouncements().stream()
                .anyMatch(announcement -> announcement.getName().equals(nameAnnouncement))) {
            return username + " has another announcement with the same name.";
        }

        Announcement newAnnouncement = new Announcement(nameAnnouncement, description);

        host.addAnnouncement(newAnnouncement);
        return username + " has successfully added new announcement.";
    }

    /**
     * Removes an announcement
     * @param nameAnnouncement for name
     * @return the result message
     */
    public String removeAnnouncement(final String nameAnnouncement) {
        if (this.getType() == null) {
            return username + " is not a host.";
        }
        if (!(this.getType().equals("host"))) {
            return username + " is not a host.";
        }

        Host host = (Host) this;

        if (Admin.getUser(username) == null) {
            return "The username " + username + " doesn't exist.";
        }

        Announcement announcementToRemove = null;

        for (Announcement announcement : host.getAnnouncements()) {
            if (announcement.getName().equals(nameAnnouncement)) {
                announcementToRemove = announcement;
                break;
            }
        }

        if (announcementToRemove == null) {
            return username + " has no announcement with the given name.";
        }

        host.removeAnnouncement(announcementToRemove);
        return username + " has successfully deleted the announcement.";
    }

    /**
     * Sets the followed playlists
     * @param followedPlaylists for followed playlists
     */
    public void setFollowedPlaylists(final ArrayList<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }

    public void buyPremium() {
        this.credits = 1000000;
    }
    public Map<String, Integer> artistsListenedTo() {
        Map<String, Integer> artistListens = new HashMap<>();
        for (Song song : premiumSongs) {
            String artistName = song.getArtist();
            artistListens.put(artistName, artistListens.getOrDefault(artistName, 0) + 1);
        }
        return artistListens;
    }
    public Map<String, Integer> songsListenedTo() {
        Map<String, Integer> songs = new HashMap<>();
        for (Song song : premiumSongs) {
            songs.put(song.getName(), songs.getOrDefault(song.getName(), 0) + 1);
        }
        return songs;
    }
    public void cancelPremium() {
        Map<String, Integer> artistsListens = this.artistsListenedTo();
        Map<String, Integer> songs = this.songsListenedTo();
        int totalListens = premiumSongs.size();
        for (Map.Entry<String, Integer> entry : artistsListens.entrySet()) {
            String artist = entry.getKey();
            Integer listens = entry.getValue();
            Artist artist1 = (Artist) Admin.getUser(artist);
            artist1.updateSongRevenue(totalListens, listens);
            for (Song song : premiumSongs) {
                if (song.matchesArtist(artist1.getUsername())) {
                    artist1.addProfitableSong(song);
                }
            }
            for (Map.Entry<String, Integer> entrySong : songs.entrySet()) {
                String songName = entrySong.getKey();
                Integer listensSong = entrySong.getValue();
                for (Song song : artist1.getProfitableSongs()) {
                    if (song.getName().equals(songName) && song.matchesArtist(artist1.getUsername())) {
                        double revenue = (double) 1000000 / listens * listensSong;
                        song.updateRevenue(revenue);
                        break;
                    }
                }
            }
            ArrayList<Song> sorted = artist1.getProfitableSongs();
            sorted.sort(
                    Comparator.comparingDouble(Song::getRevenue)
                            .reversed()
                            .thenComparing(Song::getName)
            );
            Song mostProfitable = sorted.get(0);
            double mostPorfitableValue = mostProfitable.getRevenue();
            for (Song song : artist1.getProfitableSongs()) {
                if (song.getRevenue() == mostPorfitableValue) {
                    artist1.setMostProfitableSong(song);
                    break;
                }
            }
        }
        this.credits = 0;
        this.premiumSongs.clear();
    }
    public void adBreak(Integer timestamp, Song ad) {
        if (this.player.getSource().getAudioCollection() == null
                && this.player.getSource().getAudioFile().getType().equals("song")) {
            Playlist playlist = new Playlist("adPlaylist", this.username);
            playlist.addSong((Song) this.player.getSource().getAudioFile());
            playlist.addSong(ad);
            this.player.getSource().setAudioCollection(playlist);
            this.advertisement.setAd(ad);
            this.advertisement = new Advertisement();
            this.advertisement.setBeenPlayed(true);
            return;
        }
        if (this.player.getSource().getAudioCollection().getType().equals("playlist")) {
            Playlist playlist = (Playlist) this.player.getSource().getAudioCollection();
            Song currentSong = (Song) this.player.getCurrentAudioFile();
            if (playlist.containsSong(currentSong)) {
                int currentIndex = playlist.getIndexOfTrack(currentSong);
                playlist.addSongAtIndex(currentIndex + 1, ad);
            }
            this.advertisement.setAd(ad);
            this.advertisement = new Advertisement();
            this.advertisement.setBeenPlayed(true);
            return;
        }
        if (this.player.getSource().getAudioCollection().getType().equals("album")) {
            Album album = (Album) this.player.getSource().getAudioCollection();
            Song currentSong = (Song) this.player.getCurrentAudioFile();
            if (album.containsSong(currentSong)) {
                int currentIndex = album.getIndexOfTrack(currentSong);
                album.addSongAtIndex(currentIndex + 1, ad);
            }
            this.advertisement.setAd(ad);
            this.advertisement = new Advertisement();
            this.advertisement.setBeenPlayed(true);
        }

    }
    public void addSongToAd (Song song) {
        ArrayList<Song> songs = advertisement.getSongsBetween();
        songs.add(song);
        advertisement.setSongsBetween(songs);
    }
    public void distributeMoney (ArrayList<Song> songs) {

    }
}
