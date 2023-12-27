package fileio.input;

import java.util.ArrayList;

public final class LibraryInput {
    @lombok.Getter
    private ArrayList<SongInput> songs;
    @lombok.Getter
    private ArrayList<PodcastInput> podcasts;
    @lombok.Getter
    private ArrayList<UserInput> users;

    public LibraryInput() {
    }

    public void setSongs(final ArrayList<SongInput> songs) {
        this.songs = songs;
    }

    public void setPodcasts(final ArrayList<PodcastInput> podcasts) {
        this.podcasts = podcasts;
    }

    public void setUsers(final ArrayList<UserInput> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "LibraryInput{"
                + "songs=" + songs
                + ", podcasts=" + podcasts
                + ", users=" + users
                + '}';
    }
}
