package app.statistics;

import app.Admin;
import app.audio.Files.Episode;
import app.user.User;
import java.util.Map;
import app.audio.Files.Song;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

public class UserStatistics implements Statistics {
    private final User user;
    private static final int TOP_LIMIT = 5;

    public UserStatistics(final User user) {
        this.user = user;
    }

    /**
     * @return the stats for the given user
     */
    @Override
    public JsonNode generateStatistics() {
        Admin admin = Admin.getInstance();

        List<User> allUsers = new ArrayList<>(admin.getUsers());
        allUsers.addAll(Admin.getRemovedUsers());

        Map<String, Integer> artistListens = new HashMap<>();
        Map<String, Integer> genreListens = new HashMap<>();
        Map<String, Integer> albumListens = new HashMap<>();
        Map<String, Integer> songListens = new HashMap<>();
        Map<String, Integer> episodeListens = new HashMap<>();

        List<Song> topSongs = user.getTopSongs();
        List<Episode> topEpisodes = user.getTopEpisodes();

        for (Song song : topSongs) {
            String artistName = song.getArtist();
            artistListens.put(artistName, artistListens.getOrDefault(artistName, 0) + 1);

            String genreName = song.getGenre();
            genreListens.put(genreName, genreListens.getOrDefault(genreName, 0) + 1);

            String albumName = song.getAlbum();
            albumListens.put(albumName, albumListens.getOrDefault(albumName, 0) + 1);

            String songName = song.getName();
            songListens.put(songName, songListens.getOrDefault(songName, 0) + 1);
        }
        for (Episode episode : topEpisodes) {
            String episodeName = episode.getName();
            episodeListens.put(episodeName, episodeListens.getOrDefault(episodeName, 0) + 1);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();

        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.set("topArtists",
                objectMapper.valueToTree(getTopNEntries(artistListens)));
        resultNode.set("topGenres",
                objectMapper.valueToTree(getTopNEntries(genreListens)));
        resultNode.set("topSongs",
                objectMapper.valueToTree(getTopNEntries(songListens)));
        resultNode.set("topAlbums",
                objectMapper.valueToTree(getTopNEntries(albumListens)));
        resultNode.set("topEpisodes",
                objectMapper.valueToTree(getTopNEntries(episodeListens)));
        if (topSongs.isEmpty() && topEpisodes.isEmpty()) {
            resultNode = null;
        }
        return resultNode;
    }

    /**
     * @param inputMap for the list
     * @return a list with top 5 from the given data list
     */
    private static Map<String, Integer> getTopNEntries(final Map<String,
            Integer> inputMap) {
        return inputMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(UserStatistics.TOP_LIMIT)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
