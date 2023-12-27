package fileio.input;

import app.audio.Files.Episode;
import app.audio.Files.Song;

import java.util.ArrayList;

public final class CommandInput {
    @lombok.Getter
    private String command;
    @lombok.Getter
    private String username;
    @lombok.Getter
    private Integer timestamp;
    @lombok.Getter
    private String type; // song / playlist / podcast
    @lombok.Getter
    private FiltersInput filters; // pentru search
    @lombok.Getter
    private Integer itemNumber; // pentru select
    @lombok.Getter
    private Integer repeatMode; // pentru repeat
    @lombok.Getter
    private Integer playlistId; // pentru add/remove song
    @lombok.Getter
    private String playlistName; // pentru create playlist
    @lombok.Getter
    private Integer seed; // pentru shuffle
    @lombok.Getter
    private Integer age;
    @lombok.Getter
    private String city;
    @lombok.Getter
    private String name;
    @lombok.Getter
    private Integer releaseYear;
    @lombok.Getter
    private String description;
    @lombok.Getter
    private ArrayList<Song> songs;
    @lombok.Getter
    private ArrayList<Episode> episodes;
    @lombok.Getter
    private String date;
    @lombok.Getter
    private Integer price;
    @lombok.Getter
    private String nextPage;
    public CommandInput() {
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    public void setFilters(final FiltersInput filters) {
        this.filters = filters;
    }

    public void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setRepeatMode(final Integer repeatMode) {
        this.repeatMode = repeatMode;
    }

    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }

    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    public void setSeed(final Integer seed) {
        this.seed = seed;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setSongs(final ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void setEpisodes(final ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    public void setNextPage(final String nextPage) {
        this.nextPage = nextPage;
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + '}';
    }
}
