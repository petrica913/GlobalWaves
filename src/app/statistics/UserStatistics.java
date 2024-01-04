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

    public UserStatistics(User user) {
        this.user = user;
    }

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

       // for (User user : allUsers) {
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
      //  }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();

        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.set("topArtists", objectMapper.valueToTree(getTopNEntries(artistListens, 5)));
        resultNode.set("topGenres", objectMapper.valueToTree(getTopNEntries(genreListens, 5)));
        resultNode.set("topSongs", objectMapper.valueToTree(getTopNEntries(songListens, 5)));
        resultNode.set("topAlbums", objectMapper.valueToTree(getTopNEntries(albumListens, 5)));
        resultNode.set("topEpisodes", objectMapper.valueToTree(getTopNEntries(episodeListens, 5)));

        return resultNode;
    }

    private static Map<String, Integer> getTopNEntries(Map<String, Integer> inputMap, int n) {
        return inputMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
