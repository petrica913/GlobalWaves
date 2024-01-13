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
import app.player.PlayerSource;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.user.Collections.Announcement;
import app.user.pages.HomePage;
import app.user.pages.LikedContentPage;
import app.user.pages.Page;
import app.user.pages.ArtistPage;
import app.user.pages.HostPage;
import app.user.pages.PagePrinterVisitor;
import app.user.Collections.Event;
import app.user.Collections.Merch;
import app.utils.Enums;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.LinkedHashMap;

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
    @Getter
    private ArrayList<User> subscribers;
    @Getter
    private ArrayList<Notification> notifications;
    @Getter
    private ArrayList<Merch> myMerch;
    @Getter
    private Song recommendedSong;
    @Getter
    private Playlist recommendedPlaylist;
    @Getter
    private ArrayList<Page> history;
    @Getter
    private Integer historyIndex;
    @Getter
    private ArrayList<Artist> artistsListenedTo;
    @Getter
    private LibraryEntry lastRecommended;
    @Getter
    private String lastRecomType;
    @Getter
    @Setter
    private String subscribeMessage;
    private static final int CREDIS = 1000000;
    private static final int REMAINING_TIME = 30;
    private static final int TOP5 = 5;
    private static final int TOP3 = 3;
    private static final int TOP2 = 2;


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
        subscribers = new ArrayList<>();
        notifications = new ArrayList<>();
        myMerch = new ArrayList<>();
        recommendedSong = new Song();
        recommendedPlaylist = new Playlist("", "");
        history = new ArrayList<>();
        historyIndex = -1;
        artistsListenedTo = new ArrayList<>();
    }

    /**
     * For search command
     * @param filters for filters
     * @param searchType for type
     * @return the result message
     */
    public ArrayList<String> search(final Filters filters, final String searchType) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();
        if (!online) {
            return results;
        }
        List<LibraryEntry> libraryEntries = searchBar.search(filters, searchType);

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
            nextPage = new ArtistPage(Admin.getInstance().getUser(selected.getName()));
            return "Successfully selected %s's page.".formatted(selected.getName());
        }
        if (searchBar.getLastSearchType().equals("host")) {
            nextPage = new HostPage(Admin.getInstance().getUser(selected.getName()));
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
        if (player.getSource() != null) {
            if (player.getSource().getAudioCollection() != null) {
                if (player.getSource().getAudioCollection().getType().equals("playlist")) {
                    Playlist playlist = (Playlist) player.getSource().getAudioCollection();
                    Integer adIndex = playlist.getIndexOfTrack(this.getAdvertisement().getAd());
                    playlist.removeSong(adIndex);
                }
                if (player.getSource().getAudioCollection().getType().equals("album")) {
                    Album album = (Album) player.getSource().getAudioCollection();
                    Integer adIndex = album.getIndexOfTrack(this.getAdvertisement().getAd());
                    album.removeSong(adIndex);
                }
            }
        }
        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        if (player.getSource().getAudioFile().getType().equals("song")) {
            Song song = (Song) player.getSource().getAudioFile();
            Artist artist = (Artist) Admin.getInstance().getUser(song.getArtist());
            artist.setPlay(true);
            this.artistsListenedTo.add(artist);
            topSongs.add((Song) player.getSource().getAudioFile());
            Integer listenersThis = this.getListeners();
            listenersThis++;
            this.setListeners(listenersThis);
            if (this.getCredits() != 0) {
                premiumSongs.add((Song) player.getSource().getAudioFile());
            }
            if (this.getCredits() == 0) {
                String username1 = this.getUsername();
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
            case "artist":
                String artist = ((Song) this.player.getSource().getAudioFile()).getArtist();
                User pageOwner = Admin.getInstance().getUser(artist);
                ArtistPage page = new ArtistPage(pageOwner);
                nextPage = page;
                break;
            case "host":
                String host = ((Episode) this.player.getSource().getAudioFile()).getOwner();
                User pageOwner1 = Admin.getInstance().getUser(host);
                HostPage page1 = new HostPage(pageOwner1);
                nextPage = page1;
                break;
            default:
                return username + " is trying to access a non-existent page.";
        }
        if (historyIndex >= 0) {
            history.subList(historyIndex + 1, history.size()).clear();
        }
        history.add(nextPage);
        historyIndex++;
        String pageType = nextPage.type();
        if (pageType.equals("ArtistPage")) {
            pageType = "Artist";
        } else if (pageType.equals("HostPage")) {
            pageType = "Host";
        }
        return username + " accessed " + pageType + " successfully.";
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
            PagePrinterVisitor printerVisitor = new PagePrinterVisitor();
            nextPage.accept(printerVisitor);
            return printerVisitor.getResult();
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
        for (User subscriber : artist.getSubscribers()) {
            Notification notification = new Notification("New Album", artist);
            subscriber.addNotification(notification);
        }
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

        if (Admin.getInstance().getUser(username) == null) {
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
        for (User subscriber : artist.getSubscribers()) {
            Notification notification = new Notification("New Event", artist);
            subscriber.addNotification(notification);
        }
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
        if (Admin.getInstance().getUser(username) == null) {
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
        for (User subscriber : artist.getSubscribers()) {
            Notification notification = new Notification("New Merchandise", artist);
            subscriber.addNotification(notification);
        }
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

        if (Admin.getInstance().getUser(username) == null) {
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

        if (Admin.getInstance().getUser(username) == null) {
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

    /**
     * Updates the credits for a user when the user buys premium
     */
    public void buyPremium() {
        this.credits = CREDIS;
    }

    /**
     * @return a map for with artist listened to
     */
    public Map<String, Integer> artistsListenedTo() {
        Map<String, Integer> artistListens = new HashMap<>();
        for (Song song : premiumSongs) {
            String artistName = song.getArtist();
            artistListens.put(artistName, artistListens.getOrDefault(artistName, 0) + 1);
        }
        return artistListens;
    }
    /**
     * @return a map for with songs listened to
     */
    public Map<String, Integer> songsListenedTo() {
        Map<String, Integer> songs = new HashMap<>();
        for (Song song : premiumSongs) {
            songs.put(song.getName(), songs.getOrDefault(song.getName(), 0) + 1);
        }
        return songs;
    }

    /**
     * It cancels the premium for a user
     */
    public void cancelPremium() {
        Map<String, Integer> artistsListens = this.artistsListenedTo();
        Map<String, Integer> songs = this.songsListenedTo();
        int totalListens = premiumSongs.size();
        for (Map.Entry<String, Integer> entry : artistsListens.entrySet()) {
            String artist = entry.getKey();
            Integer listens = entry.getValue();
            Artist artist1 = (Artist) Admin.getInstance().getUser(artist);
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
                    if (song.getName().equals(songName)
                            && song.matchesArtist(artist1.getUsername())) {
                        double revenue = (double) CREDIS / listens * listensSong;
                        song.updateRevenue(revenue);
                        break;
                    }
                }
            }

        }
        this.credits = 0;
        this.premiumSongs.clear();
    }

    /**
     * It is used for adding a break
     * @param timestamp for the timestamp when an ad is added
     * @param ad for the ad
     * @param price for the ad's price
     */
    public void adBreak(final Integer timestamp, final Song ad, final Integer price) {
        if (this.player.getSource().getAudioCollection() == null
                && this.player.getSource().getAudioFile().getType().equals("song")) {
            Playlist playlist = new Playlist("adPlaylist", this.username);
            playlist.addSong((Song) this.player.getSource().getAudioFile());
            playlist.addSong(ad);
            Integer remainedTime = player.getSource().getRemainedDuration();
            PlayerSource source = this.player.getSource();
            source.setAudioCollection(playlist);
            source.setType(Enums.PlayerSourceType.PLAYLIST);
            this.player.setSource(source);
            this.player.sourceTime(remainedTime);
            this.advertisement.setAd(ad);
            this.advertisement.setPrice(price);
            return;
        }
        if (this.player.getSource().getAudioCollection().getType().equals("playlist")) {
            Playlist playlist = (Playlist) this.player.getSource().getAudioCollection();
            Song currentSong = (Song) this.player.getCurrentAudioFile();
            if (playlist.containsSong(currentSong)) {
                int currentIndex = playlist.getIndexOfTrack(currentSong);
                playlist.addSongAtIndex(currentIndex + 1, ad);
            }
            PlayerSource source = this.player.getSource();
            source.setAudioCollection(playlist);
            this.player.setSource(source);
            this.advertisement.setAd(ad);
            this.advertisement.setPrice(price);
            return;
        }
        if (this.player.getSource().getAudioCollection().getType().equals("album")) {
            Album album = (Album) this.player.getSource().getAudioCollection();
            Song currentSong = (Song) this.player.getCurrentAudioFile();
            if (album.containsSong(currentSong)) {
                int currentIndex = album.getIndexOfTrack(currentSong);
                album.addSongAtIndex(currentIndex + 1, ad);
            }
            PlayerSource source = this.player.getSource();
            source.setAudioCollection(album);
            this.player.setSource(source);
            this.advertisement.setAd(ad);
            this.advertisement.setPrice(price);
        }

    }

    /**
     * It is used to add a song in the list of
     * songs that were listened to between 2 ads
     * @param song for the song
     */
    public void addSongToAd (final Song song) {
        ArrayList<Song> songs = advertisement.getSongsBetween();
        songs.add(song);
        advertisement.setSongsBetween(songs);
    }
    @Getter
    private Integer total = 0;
    @Getter
    private Integer aListen = 0;

    /**
     * It is used to distribute money to the artists
     * @param songs
     */
    public void distributeMoney (final ArrayList<Song> songs) {
        Map<String, Integer> artistListens = new HashMap<>();
        Map<String, Integer> songListens = new HashMap<>();
        for (Song song : songs) {
            String artistName = song.getArtist();
            String songName = song.getName();
            artistListens.put(artistName, artistListens.getOrDefault(artistName, 0) + 1);
            songListens.put(songName, songListens.getOrDefault(songName, 0) + 1);
        }
        Integer totalListens = songs.size();
        for (Map.Entry<String, Integer> entry : artistListens.entrySet()) {
            String artist = entry.getKey();
            Integer listens = entry.getValue();
            Artist artist1 = (Artist) Admin.getInstance().getUser(artist);
            total = totalListens;
            aListen = listens;
            artist1.updateSongRevenueFree(totalListens, listens,
                    this.getAdvertisement().getPrice());
        }
    }

    /**
     * It is used for subscribing a user
     * @param user for the given user
     */
    public void subscribe(final User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
        } else {
            subscribers.remove(user);
        }
    }

    /**
     * For adding a notification in user notifications
     * @param notification for the given notification
     */
    public void addNotification(final Notification notification) {
        notifications.add(notification);
    }

    /**
     * Used for resetting the user's notifications
     */
    public void resetNotifications() {
        this.notifications = new ArrayList<>();
    }

    /**
     * Used when a user buys merch from an artist
     * @param merchName for the merch name
     * @param pageOwner for the owner of the user's current page
     * @return the info about the act of buying merch
     */
    public String buyMerch(final String merchName, final User pageOwner) {
        String message = null;
        Artist artist = null;
        if (pageOwner != null) {
            artist = (Artist) pageOwner;
        }
        if (artist == null) {
            return null;
        }
        for (Merch merch : artist.getMerches()) {
            if (merch.getName().equals(merchName)) {
                artist.updateMerchRevenue(merch);
                this.myMerch.add(merch);
                message = username + " has added new merch successfully.";
                break;
            }
        }
        if (message == null) {
            message = "The merch " + merchName + " doesn't exist.";
        }
        return message;
    }

    /**
     * @param type for the type of recommendation
     * @return the info about the act of updating the recommendations
     */
    public String updateRecom(final String type) {
        String message = null;
        if (type.equals("random_song")) {
            if (this.player.getSource().getRemainedDuration() > REMAINING_TIME) {
                Integer seed = this.player.getSource().getRemainedDuration();
                Admin admin = Admin.getInstance();
                Song currentSong = (Song) this.player.getSource().getAudioFile();
                seed = currentSong.getDuration() - seed;
                Random random = new Random(seed);
                String currentGenre = currentSong.getGenre();
                List<Song> adminSongs = admin.getSongs();
                List<Song> songs = new ArrayList<>();
                for (Song song : adminSongs) {
                    if (song.getGenre().equals(currentGenre) && !songs.contains(song)) {
                        songs.add(song);
                    }
                }
                int randomIndex = random.nextInt(songs.size());
                this.recommendedSong = songs.get(randomIndex);
                lastRecomType = "song";
                lastRecommended = recommendedSong;
                message = "The recommendations for user " + username
                        + " have been updated successfully.";
            }
        } else if (type.equals("random_playlist")) {
            Playlist playlist = this.createRandomPlaylist();
            this.recommendedPlaylist = playlist;
            lastRecomType = "playlist";
            lastRecommended = recommendedPlaylist;
            message = "The recommendations for user " + username
                    + " have been updated successfully.";
        } else if (type.equals("fans_playlist")) {
            String artistName = ((Song)this.player.getCurrentAudioFile()).getArtist();
            Artist artist = (Artist) Admin.getInstance().getUser(artistName);
            this.recommendedPlaylist = artist.fansPlaylist();
            lastRecomType = "playlist";
            lastRecommended = recommendedPlaylist;
            message = "The recommendations for user " + username
                    + " have been updated successfully.";
        }
        return message;
    }

    /**
     * @return a random playlist
     */
    public Playlist createRandomPlaylist() {
        ArrayList<Song> listOfSongs = new ArrayList<>();
        listOfSongs.addAll(this.getLikedSongs());
        for (Playlist playlist : this.playlists) {
            listOfSongs.addAll(playlist.getSongs());
        }
        for (Playlist playlist : this.getFollowedPlaylists()) {
            listOfSongs.addAll(playlist.getSongs());
        }

        Map<Song, Integer> songCountMap = new HashMap<>();
        Map<String, Integer> genreCountMap = new HashMap<>();
        for (Song song : listOfSongs) {
            songCountMap.put(song, songCountMap.getOrDefault(song, 0) + 1);
            genreCountMap.put(song.getGenre(),
                    genreCountMap.getOrDefault(song.getGenre(), 0) + 1);
        }
        List<Map.Entry<String, Integer>> sortedGenreList = genreCountMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
        String genre1 = new String(), genre2 = new String(), genre3 = new String();
        if (sortedGenreList.size() > 0) {
            genre1 = sortedGenreList.get(0).getKey();
        }
        if (sortedGenreList.size() > 1) {
            genre2 = sortedGenreList.get(1).getKey();
        }
        if (sortedGenreList.size() > 2) {
            genre3 = sortedGenreList.get(2).getKey();
        }
        ArrayList<Song> genre1Songs = new ArrayList<>();
        ArrayList<Song> genre2Songs = new ArrayList<>();
        ArrayList<Song> genre3Songs = new ArrayList<>();

        for (Song song : listOfSongs) {
            if (song.getGenre().equals(genre1) && !genre1Songs.contains(song)) {
                genre1Songs.add(song);
            }
            if (song.getGenre().equals(genre2) && !genre2Songs.contains(song)) {
                genre2Songs.add(song);
            }
            if (song.getGenre().equals(genre3) && !genre3Songs.contains(song)) {
                genre3Songs.add(song);
            }
        }
        Map<Song, Integer> genre1Map = genreMap(genre1Songs);
        Map<Song, Integer> genre2Map = genreMap(genre2Songs);
        Map<Song, Integer> genre3Map = genreMap(genre3Songs);
        Playlist playlist = new Playlist(username + "'s recommendations", username);
        addTopSongsToPlaylist(genre1Map, playlist, TOP5);
        addTopSongsToPlaylist(genre2Map, playlist, TOP3);
        addTopSongsToPlaylist(genre3Map, playlist, TOP2);
        return playlist;
    }

    /**
     * Used for adding top songs to a playlist
     * @param songMap for the song map
     * @param playlist for the playlist where the songs will be added
     * @param topCount for the count of songs
     */
    private void addTopSongsToPlaylist(final Map<Song, Integer> songMap,
                                       final Playlist playlist, final int topCount) {
        int count = 0;
        for (Map.Entry<Song, Integer> entry : songMap.entrySet()) {
            playlist.addSong(entry.getKey());
            count++;
            if (count == topCount) {
                break;
            }
        }
    }

    /**
     * @param songs for the songs
     * @return a map of genres that were listened to
     */
    private Map<Song, Integer> genreMap (final ArrayList<Song> songs) {
        Map<Song, Integer> songLikesMap = new HashMap<>();
        for (Song song : songs) {
            songLikesMap.put(song, song.getLikes());
        }
        return songLikesMap.entrySet()
                .stream()
                .sorted(Map.Entry.<Song, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Adds an artist to the list of the listened artists
     * @param artist for the artist
     */
    public void addArtist(final Artist artist) {
        this.artistsListenedTo.add(artist);
    }
    public String previousPage() {
        String message = new String();
        if (historyIndex < 0) {
            message = "There are no pages left to go back.";
            return message;
        }
        historyIndex--;
        nextPage = history.get(historyIndex);
        message = "The user " + this.username
                + " has navigated successfully to the previous page.";
        return message;
    }

    /**
     * @return the info about the act of changing the page to the next one
     */
    public String nextPage() {
        String message = new String();
        historyIndex++;
        if (historyIndex >= history.size()) {
            message = "There are no pages left to go forward.";
            return message;
        }
        nextPage = history.get(historyIndex);
        message = "The user " + this.username
                + " has navigated successfully to the next page.";
        return message;
    }

    /**
     * @return the info about the act of loading recommendations
     */
    public String loadRecommendations() {
        if (lastRecommended == null) {
            return "No recommendations available.";
        }
        if (player.getSource() != null) {
            if (player.getSource().getAudioCollection() != null) {
                if (player.getSource().getAudioCollection().getType().equals("playlist")) {
                    Playlist playlist = (Playlist) player.getSource().getAudioCollection();
                    Integer adIndex = playlist.getIndexOfTrack(this.getAdvertisement().getAd());
                    playlist.removeSong(adIndex);
                }
                if (player.getSource().getAudioCollection().getType().equals("album")) {
                    Album album = (Album) player.getSource().getAudioCollection();
                    Integer adIndex = album.getIndexOfTrack(this.getAdvertisement().getAd());
                    album.removeSong(adIndex);
                }
            }
        }
        player.setSource(lastRecommended, lastRecomType);

        if (player.getSource().getAudioFile().getType().equals("song")) {
            Song song = (Song) player.getSource().getAudioFile();
            Artist artist = (Artist) Admin.getInstance().getUser(song.getArtist());
            artist.setPlay(true);
            this.artistsListenedTo.add(artist);
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
}
