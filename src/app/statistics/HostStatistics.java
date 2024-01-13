package app.statistics;

import app.Admin;
import app.user.Host;
import app.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HostStatistics implements Statistics {
    private final Host host;
    private static final int TOP_EPISODES_LIMIT = 5;

    public HostStatistics(final Host host) {
        this.host = host;
    }

    /**
     * @return the stats for the host
     */
    @Override
    public JsonNode generateStatistics() {
        Admin admin = Admin.getInstance();

        List<User> allUsers = new ArrayList<>(admin.getUsers());
        allUsers.addAll(Admin.getRemovedUsers());

        List<User> hostFans = allUsers.stream()
                .filter(user -> user.getTopEpisodes().stream()
                        .anyMatch(episode -> episode.getOwner().equals(host.getUsername())))
                .collect(Collectors.toList());
        Map<String, Integer> episodeListens = new HashMap<>();
        for (User user : hostFans) {
            List<String> userEpisodes = user.getTopEpisodes().stream()
                    .filter(episode -> episode.getOwner().equals(host.getUsername()))
                    .map(episode -> episode.getName())
                    .collect(Collectors.toList());

            for (String episode : userEpisodes) {
                episodeListens.put(episode, episodeListens.getOrDefault(episode, 0) + 1);
            }
        }
        int totalListeners = hostFans.size();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
        ObjectNode resultNode = objectMapper.createObjectNode();
        resultNode.set("topEpisodes", createTopNode(episodeListens));
        resultNode.put("listeners", totalListeners);
        if (totalListeners == 0) {
            resultNode = null;
        }

        return resultNode;
    }

    /**
     * @param data for the total episodes of the host
     * @return the top 5 episodes
     */
    private JsonNode createTopNode(final Map<String, Integer> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();

        data.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(TOP_EPISODES_LIMIT)
                .forEach(entry -> node.put(entry.getKey(), entry.getValue()));

        return node;
    }
}
