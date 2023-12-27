package app.user.pages;

import app.Admin;
import app.audio.Collections.Album;
import app.user.Artist;
import app.user.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistPage implements Page {
    @Getter
    private final User user;
    private final Artist artist;

    public ArtistPage(final User user) {
        this.user = user;
        this.artist = (Artist) Admin.getUser(user.getUsername());
    }

    /**
     * @return the content of the page
     */
    @Override
    public String display() {
        return "Albums:\n\t" + displayAlbums() + "\n\n"
                + "Merch:\n\t" + displayMerch() + "\n\n"
                + "Events:\n\t" + displayEvents();
    }

    /**
     * @return the page type
     */
    @Override
    public String type() {
        return "ArtistPage";
    }

    /**
     * Display the albums of an artist
     * @return the result
     */
    private String displayAlbums() {
        List<String> albumNames = artist.getAlbums().stream()
                .map(Album::getName).collect(Collectors.toList());
        return albumNames.toString();
    }

    /**
     * Display the merch of an artist
     * @return the result
     */
    private String displayMerch() {
        List<String> merchItems = artist.getMerches().stream()
                .map(merch -> merch.getName() + " - " + merch.getPrice()
                        + ":\n\t" + merch.getDescription()).collect(Collectors.toList());
        return merchItems.toString();
    }

    /**
     * Display the events
     * @return the result
     */
    private String displayEvents() {
        List<String> formattedEvents = artist.getEvents().stream()
                .map(event -> event.getName() + " - " + event.getDate()
                        + ":\n\t" + event.getDescription()).collect(Collectors.toList());
        return formattedEvents.toString();
    }

}
