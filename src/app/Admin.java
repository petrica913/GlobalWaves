package app;

import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Collections.Album;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PodcastBookmark;
import app.statistics.Factory;
import app.statistics.Statistics;
import app.statistics.StatisticsFactory;
import app.user.User;
import app.user.Artist;
import app.user.Host;
import app.user.pages.ArtistPage;
import app.user.pages.HostPage;
import app.user.pages.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class Admin {
    private Admin() {
    }
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    @Getter
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;
    private static final int MAGIC_CONT = 5;
    private static Admin instance = null;
    @Getter
    private static List<User> removedUsers = new ArrayList<>();
    @Setter
    @Getter
    private Integer albumsCount = 0;
    @Setter
    @Getter
    private Integer artistsCount = 0;

    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    /**
     * Sets the users
     * @param userInputList for input list of users
     */
    public void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets the songs
     * @param songInputList for the input list of songs
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Sets the podcasts
     * @param podcastInputList for the input list of podcasts
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * @return all songs
     */
    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    /**
     * @return all podcasts
     */
    public List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * @return all playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * @return all users
     */
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Sets the albums
     * @param albums for albums
     */
    public void setAlbums(final List<Album> albums) {
        Admin.albums = albums;
    }

    /**
     * @param username for username
     * @return the user that has the name username
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Updates the timestamp
     * @param newTimestamp for new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * @return top 5 of most liked songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= MAGIC_CONT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * @return top 5 of most liked playlists
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getInstance().getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= MAGIC_CONT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * @return top 5 of most liked albums
     */
    public List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        sortedAlbums.sort(Comparator.comparingInt(Album::getFollowers)
                .reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= MAGIC_CONT) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    /**
     * @return top 5 most appreciated artists
     */
    public List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>();
        for (User user : Admin.getInstance().getUsers()) {
            if (user.getType() == null) {
                user.setType("user");
            }
            if (user.getType().equals("artist")) {
                sortedArtists.add((Artist) user);
                for (Album album : ((Artist) user).getAlbums()) {
                    Integer numberOfLikes = ((Artist) user).getLikes();
                    numberOfLikes += album.getFollowers();
                    ((Artist) user).setLikes(numberOfLikes);
                }
            }
        }
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes)
                .reversed()
                .thenComparing(Artist::getUsername, Comparator.naturalOrder()));
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= MAGIC_CONT) {
                break;
            }
            topArtists.add(artist.getUsername());
            count++;
        }
        return topArtists;
    }

    /**
     * @return all online users
     */
    public List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == null) {
                user.setType("user");
            }
            if (user.isOnline() && !(user.getType().equals("artist")) && !(user.getType().equals("host"))) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    /**
     * @return all users
     */
    public List<String> getAllUsers() {
        List<String> allUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getType() == null) {
                user.setType("user");
            }
            if (!(user.getType().equals("artist")) && !(user.getType().equals("host"))) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user : users) {
            if (user.getType() == null) {
                user.setType("user");
            }
            if (user.getType().equals("artist")) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user : users) {
            if (user.getType() == null) {
                user.setType("user");
            }
            if (user.getType().equals("host")) {
                allUsers.add(user.getUsername());
            }
        }
        return allUsers;
    }

    /**
     * Adds a new user
     * @param commandInput for commandInput
     * @return the message generated by trying to add a new user
     */
    public String addUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        String type = commandInput.getType();
        int age = commandInput.getAge();
        String city = commandInput.getCity();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return "The username " + username + " is already taken.";
            }
        }
        switch (type) {
            case "user":
                User user = new User(username, age, city);
                user.setType("user");
                users.add(user);
                break;
            case "artist":
                Artist artist = new Artist(username, age, city);
                artist.setType("artist");
                artistsCount++;
                artist.setOrder(artistsCount);
                users.add(artist);
                break;
            case "host":
                Host host = new Host(username, age, city);
                host.setType("host");
                users.add(host);
                break;
            default:
                return "Invalid user type.";
        }

        return "The username " + username + " has been added successfully.";
    }

    /**
     * Removes a user
     * @param commandInput for commandInput
     * @return the message generated by trying to remove a user
     */
    public String removeUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();

        User userToRemove = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                userToRemove = user;
                break;
            }
        }

        if (userToRemove == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (!canUserBeDeleted(userToRemove)) {
            return username + " can't be deleted.";
        }
        if (userToRemove.getType() == null) {
            removedUsers.add(userToRemove);
            users.remove(userToRemove);
            for (User user : users) {
                for (Playlist playlist : userToRemove.getPlaylists()) {
                    ArrayList<Playlist> playlists = user.getPlaylists();
                    playlists.remove(playlist);
                    user.setFollowedPlaylists(playlists);
                }
            }
            for (Playlist playlist : Admin.getInstance().getPlaylists()) {
                if (userToRemove.getFollowedPlaylists().contains(playlist)) {
                    playlist.decreaseFollowers();
                }
            }
            return username + " was successfully deleted.";
        }
        if (userToRemove.getType().equals("user")) {
            users.remove(userToRemove);
            for (User user : users) {
                for (Playlist playlist : userToRemove.getPlaylists()) {
                    ArrayList<Playlist> playlists = user.getPlaylists();
                    playlists.remove(playlist);
                    user.setFollowedPlaylists(playlists);
                }
            }
            for (Playlist playlist : Admin.getInstance().getPlaylists()) {
                if (userToRemove.getFollowedPlaylists().contains(playlist)) {
                    playlist.decreaseFollowers();
                }
            }
            return username + " was successfully deleted.";
        }
        Iterator<Song> songIterator = songs.iterator();
        while (songIterator.hasNext()) {
            Song song = songIterator.next();
            if (song.getArtist().equals(userToRemove.getUsername())) {
                songIterator.remove();
            }
        }

        Iterator<Podcast> podcastIterator = podcasts.iterator();
        while (podcastIterator.hasNext()) {
            Podcast podcast = podcastIterator.next();
            if (podcast.getOwner().equals(userToRemove.getUsername())) {
                podcastIterator.remove();
            }
        }

        Iterator<Album> albumIterator = albums.iterator();
        while (albumIterator.hasNext()) {
            Album album = albumIterator.next();
            if (album.getOwner().equals(userToRemove.getUsername())) {
                ((Artist) userToRemove).removeAlbum(album.getName());
                albumIterator.remove();
            }
        }
        for (User user : users) {
            List<Song> likedSongs = new ArrayList<>(user.getLikedSongs());
            for (Song likedSong : likedSongs) {
                if (!songs.contains(likedSong)) {
                    ArrayList<Song> newList = user.getLikedSongs();
                    newList.remove(likedSong);
                    user.setLikedSongs(newList);
                }
            }
        }
        users.remove(userToRemove);
        return username + " was successfully deleted.";
    }

    /**
     * Checks if a user can be removed
     * @param userToRemove for the user to be removed
     * @return a boolean
     */
    public static boolean canUserBeDeleted(final User userToRemove) {
        boolean returnValue = true;
        for (User user : users) {
            AudioFile audioFile = user.getPlayer().getCurrentAudioFile();
            AudioCollection audioCollection = null;
            if (user.getPlayer().getSource() != null) {
                 audioCollection = user.getPlayer().getSource().getAudioCollection();
            }
            Artist artist = null;
            Host host = null;
            if (userToRemove.getType() == null) {
                userToRemove.setType("user");
            }
            if (userToRemove.getType().equals("artist")) {
                artist = (Artist) userToRemove;
            }
            if (userToRemove.getType().equals("host")) {
                host = (Host) userToRemove;
            }
            if (audioFile != null) {
                if (user.getPlayer().getType().equals("song")) {
                    if (audioFile.matchesArtist(userToRemove.getUsername())) {
                        returnValue = false;
                        break;
                    }
                }
                if (user.getPlayer().getType().equals("podcast")) {
                    if (audioFile.matchesOwner(userToRemove.getUsername())) {
                        returnValue = false;
                        break;
                    }
                }
                if (user.getPlayer().getType().equals("album")) {
                    if (audioFile.matchesArtist(userToRemove.getUsername())) {
                        returnValue = false;
                        break;
                    }
                }
                if (user.getPlayer().getType().equals("playlist")) {
                    if (audioFile.matchesOwner(userToRemove.getUsername())) {
                        returnValue = false;
                        break;
                    }
                    if (user.getPlayer().getSource().getAudioFile()
                            .matchesArtist(userToRemove.getUsername())) {
                        returnValue = false;
                        break;
                    }
                }
            }
            if (audioCollection != null) {
                if (audioCollection.matchesOwner(userToRemove.getUsername())) {
                    returnValue = false;
                    break;
                }
            }
            if (user.getPlayer().getType() != null) {
                if (user.getPlayer().getType().equals("podcast")
                        && user.getPlayer().getBookmarks() != null
                        && userToRemove.getType().equals("host")) {
                    for (PodcastBookmark podcastBookmark : user.getPlayer().getBookmarks()) {
                        Host hostToRemove = (Host) userToRemove;
                        for (Podcast podcast : hostToRemove.getPodcasts()) {
                            if (podcast.getName().equals(podcastBookmark.getName())) {
                                returnValue = false;
                                break;
                            }
                        }
                        if (!returnValue) {
                            break;
                        }
                    }
                }
            }
            Page page = user.getNextPage();
            if (page.type().equals("ArtistPage")) {
                if (((ArtistPage) user.getNextPage()).getUser().getUsername()
                        .equals(userToRemove.getUsername())) {
                    returnValue = false;
                    break;
                }
            }
            if (page.type().equals("HostPage")) {
                if (((HostPage) user.getNextPage()).getUser().getUsername()
                        .equals(userToRemove.getUsername())) {
                    returnValue = false;
                    break;
                }
            }
        }
        return returnValue;
    }

    /**
     * For adding new songs to the general list of songs
     * @param newSongs for new list of songs
     */
    public void addSongs(final List<Song> newSongs) {
        songs.addAll(newSongs);
    }

    /**
     * Resets the app
     */
    public void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }
    public JsonNode wrapped(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        User user = getUser(username);
        JsonNode result;
        Statistics userStats = null;
        StatisticsFactory factory = new Factory();
        assert user != null;
        if (user.getType() == null) {
            user.setType("user");
        }
        if (user.getType().equals("user")) {
            userStats = factory.createUserStatistics(user);
        }
        if (user.getType().equals("host")) {
            userStats = factory.createHostStatistics((Host) user);
        }
        if (user.getType().equals("artist")) {
            userStats = factory.createArtistStatistics((Artist) user);
        }
        assert userStats != null;
        result = userStats.generateStatistics();
        return result;
    }
    public JsonNode endProgram() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        ArrayList<Artist> artistsList = new ArrayList<>();

        for (User user : Admin.getInstance().getUsers()) {
            if (user.getType() == null || !user.getType().equals("artist")) {
                continue;
            }

            Artist artist = (Artist) user;
            artistsList.add(artist);
        }

        artistsList.sort(Comparator
                .comparing(Artist::getSongRevenue)
                .thenComparing(Comparator.comparing(Artist::getUsername, Comparator.naturalOrder()))
        );

        int count = 1;

        for (Artist artist : artistsList) {
            if (!(artist.isPlay() || artist.isBoughtMerch())) {
                continue;
            }

            artist.setRanking(count);
            ObjectNode artistStats = objectMapper.createObjectNode();
            artistStats.put("merchRevenue", artist.getMerchRevenue());
            artistStats.put("songRevenue", artist.getSongRevenue());
            artistStats.put("ranking", artist.getRanking());
            artistStats.put("mostProfitableSong", artist.getMostProfitableSong());

            result.set(artist.getUsername(), artistStats);
            count++;
        }

        return result;
    }

}
