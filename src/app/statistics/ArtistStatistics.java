package app.statistics;

import app.Admin;
import app.user.Artist;
import app.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArtistStatistics implements Statistics {
    private final Artist artist;

    public ArtistStatistics(final Artist artist) {
        this.artist = artist;
    }

    @Override
    public JsonNode generateStatistics() {
        Admin admin = Admin.getInstance();

        List<User> allUsers = new ArrayList<>(admin.getUsers());
        allUsers.addAll(Admin.getRemovedUsers());

        List<User> artistFans = allUsers.stream()
                .filter(user -> user.getTopSongs().stream()
                        .anyMatch(song -> song.getArtist().equals(artist.getUsername())))
                .collect(Collectors.toList());

        Map<String, Integer> albumListens = new HashMap<>();
        for (User user : artistFans) {
            List<String> userAlbums = user.getTopSongs().stream()
                    .filter(song -> song.getArtist().equals(artist.getUsername()))
                    .map(song -> song.getAlbum())
                    .collect(Collectors.toList());

            for (String album : userAlbums) {
                albumListens.put(album, albumListens.getOrDefault(album, 0) + 1);
            }
        }

        Map<String, Integer> songListens = new HashMap<>();
        for (User user : artistFans) {
            List<String> userSongs = user.getTopSongs().stream()
                    .filter(song -> song.getArtist().equals(artist.getUsername()))
                    .map(song -> song.getName())
                    .collect(Collectors.toList());

            for (String song : userSongs) {
                songListens.put(song, songListens.getOrDefault(song, 0) + 1);
            }
        }

        List<String> topFans = artistFans.stream()
                .collect(Collectors.toMap(
                        User::getUsername,
                        user -> user.getTopSongs().stream()
                                .filter(song -> song.getArtist().equals(artist.getUsername()))
                                .mapToInt(song -> 1)
                                .sum()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int totalListeners = artistFans.size();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();

        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.set("topAlbums", createTopNode(albumListens));
        resultNode.set("topSongs", createTopNode(songListens));
        resultNode.set("topFans", objectMapper.valueToTree(topFans));
        resultNode.put("listeners", totalListeners);
        if (totalListeners == 0) {
            resultNode = null;
        }

        return resultNode;
    }

    private JsonNode createTopNode(Map<String, Integer> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();

        data.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(5)
                .forEach(entry -> node.put(entry.getKey(), entry.getValue()));

        return node;
    }
}
