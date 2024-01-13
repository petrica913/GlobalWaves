package app.player;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import app.audio.Files.Song;
import app.audio.Files.Episode;
import app.user.Advertisement;
import app.user.Artist;
import app.user.User;
import app.utils.Enums;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Enums.RepeatMode repeatMode;
    private boolean shuffle;
    private boolean paused;
    @Setter
    @Getter
    private PlayerSource source;
    @Setter
    @Getter
    private String type;
    private static final int PODCAST_BACKWARD = 90;
    private static final int PODCAST_FORWARD = -90;

    @Getter
    private ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();

    @Getter
    private ArrayList<Song> topSongs = new ArrayList<>();
    @Getter
    private ArrayList<Episode> topEpisodes = new ArrayList<>();
    @Getter
    @Setter
    private User owner;

    public Player() {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
    }

    /**
     * For stopping a player
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }

    /**
     * For updating the player's podcast bookmark
     */
    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark = new PodcastBookmark(source
                    .getAudioCollection().getName(), source.getIndex(), source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }

    /**
     * Creates a source for the player
     * @param type for typer
     * @param entry for entry
     * @param bookmarks for bookmarks
     * @return the source
     */
    public static PlayerSource createSource(final String type, final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        } else if ("album".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.ALBUM, (AudioCollection) entry);
        }

        return null;
    }

    /**
     * Creates a podcast source
     * @param collection for collection
     * @param bookmarks for bookmarks
     * @return the podcast source
     */
    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }

    /**
     * Sets the source of a player
     * @param entry for entry
     * @param typeNum for type
     */
    public void setSource(final LibraryEntry entry, final String typeNum) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = typeNum;
        this.source = createSource(type, entry, bookmarks);
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
    }

    /**
     * Puts the player on pause
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * Shuffles the source
     * @param seed for seed
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
            if (source.getType() == Enums.PlayerSourceType.PLAYLIST) {
                Playlist playlist = (Playlist) source.getAudioCollection();
                playlist.setSeed(seed);
            }
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
        if (source.getType() == Enums.PlayerSourceType.ALBUM) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }

    /**
     * Sets the repeat mode
     * @return repeatMode
     */
    public Enums.RepeatMode repeat() {
        if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
            if (source.getType() == Enums.PlayerSourceType.LIBRARY) {
                repeatMode = Enums.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Enums.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                    repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Enums.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }
    /**
     * Simulates the time for a player
     * @param time for time
     */
    public void simulatePlayer(int time) {
        if (!paused) {
            while (time >= source.getDuration()) {
                boolean adPlayed = false;
                boolean currentAd = false;
                if (source.getAudioFile().getName().equals("Ad Break")) {
                    currentAd = true;
                }
                time -= source.getDuration();
                next();
                if (source != null){
                    if (source.getAudioFile().getType().equals("song") &&
                            source.getAudioCollection() != null &&
                            !this.getSource().getAudioFile().getName().equals("Ad Break")) {
                        Song song = (Song) this.getSource().getAudioFile();
                        Artist artist1 = (Artist) Admin.getInstance().getUser(song.getArtist());
                        if (artist1 != null) {
                            artist1.setPlay(true);
                        }
                        topSongs.add((Song) this.getCurrentAudioFile());
                        owner.addArtist(artist1);
                        String artistName = ((Song) source.getAudioFile()).getArtist();
                        Artist artist = (Artist) Admin.getUser(artistName);
                        if (artist != null) {
                            artist.setPlay(true);
                            Integer listeners = artist.getListeners();
                            listeners++;
                            artist.setListeners(listeners);
                        }
                        if (owner.getType() == null) {
                            owner.setType("user");
                        }
                        if (owner.getType().equals("user") && owner.getCredits() != 0) {
                            ArrayList<Song> premiumSongs = owner.getPremiumSongs();
                            premiumSongs.add((Song) this.getCurrentAudioFile());
                            owner.setPremiumSongs(premiumSongs);
                        }
                        if (owner.getType().equals("user") && owner.getCredits() == 0) {
                            String ownerName = owner.getUsername();
                            owner.addSongToAd((Song) this.getCurrentAudioFile());
                        }
                        // aici vezi daca indexu din audiocollection al sourcei e mai mare decat ad-u
                        Song ad = owner.getAdvertisement().getAd();
                        if (source.getAudioFile().getName().equals(ad.getName())) {
                            owner.getAdvertisement().setBeenPlayed(true);
                        }
                        Song currentSong = (Song) source.getAudioFile();
                        if (source.getAudioCollection().equals("playlist")
                                && owner.getAdvertisement().isBeenPlayed()) {
                            Playlist playlist = (Playlist) source.getAudioCollection();
                            Integer currentIndex = playlist.getIndexOfTrack(currentSong);
                            Integer adIndex = playlist.getIndexOfTrack(owner.getAdvertisement().getAd());
                            if (currentIndex > adIndex && owner.getAdvertisement().isBeenPlayed()) {
                                playlist.removeSong(adIndex);
                                owner.distributeMoney(owner.getAdvertisement().getSongsBetween());
                                setArtistsProfitable(owner.getAdvertisement().getSongsBetween(),
                                        owner.getTotal(), owner.getAListen());
                                owner.setAdvertisement(new Advertisement());
                            }
                        }
                        if (source.getAudioCollection().equals("album")
                                && owner.getAdvertisement().isBeenPlayed()) {
                            Album album = (Album) source.getAudioCollection();
                            Integer currentIndex = album.getIndexOfTrack(currentSong);
                            Integer adIndex = album.getIndexOfTrack(owner.getAdvertisement().getAd());
                            if (currentIndex > adIndex && owner.getAdvertisement().isBeenPlayed()) {
                                album.removeSong(adIndex);
                                owner.distributeMoney(owner.getAdvertisement().getSongsBetween());
                                setArtistsProfitable(owner.getAdvertisement().getSongsBetween(),
                                        owner.getTotal(), owner.getAListen());                                owner.setAdvertisement(new Advertisement());
                            }
                        }
                    }
                    if (source.getAudioFile().getType().equals("episode")) {
                        topEpisodes.add((Episode) this.getCurrentAudioFile());
                    }

                }
                if (paused) {
                    if (source == null && currentAd == true) {
                        owner.distributeMoney(owner.getAdvertisement().getSongsBetween());
                        setArtistsProfitable(owner.getAdvertisement().getSongsBetween(),
                                owner.getTotal(), owner.getAListen());
                        owner.setAdvertisement(new Advertisement());
                    }
                    break;
                }
            }
            if (!paused) {
                source.skip(-time);
            }

        }
    }

    /**
     * Sets the audio file as the next audio file
     */
    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);

        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }

    /**
     * Sets the audio file as the previous file
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }

    /**
     * Skips the current audio file
     * @param duration for duration
     */
    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }

    /**
     * Skips 90 periods forward for a podcast
     */
    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(PODCAST_FORWARD);
        }
    }

    /**
     * Sets the podcast with 90 periods backwards
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(PODCAST_BACKWARD);
        }
    }

    /**
     * @return current audio file
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }
        return source.getAudioFile();
    }

    /**
     * @return if the player is on pause or not
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * @return if the player is on shuffle or not
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * @return the stats of a player
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }
    public void sourceTime(Integer time) {
        source.setRemainedDuration(time);
    }
    public void setArtistsProfitable(ArrayList<Song> songs,
                                     Integer totalListens, Integer listens) {
        for (Song song : songs) {
            Artist artist = (Artist) Admin.getInstance().getUser(song.getArtist());
            ArrayList<Song> artistSongs = artist.getFreeProfitableSongs();
            artistSongs.add(song);
            artist.setFreeProfitableSongs(artistSongs);
            double revenue = (double) owner.getAdvertisement().getPrice() / totalListens * listens;
            song.updateRevenue(revenue);
        }
    }
}
