package app;

import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.Artist;
import app.user.CommandSubscribe.SubscribeFunction;
import app.user.Host;
import app.user.User;
import app.user.pages.ArtistPage;
import app.user.pages.HostPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CommandRunner {
    private CommandRunner() {
    }
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * For search command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = user.search(filters, type);
        String message = "Search returned " + results.size() + " results";
        if (!user.isOnline()) {
            message = user.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * For select command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For load command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For playPause command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For repeat command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For shuffle command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For forward command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For backward command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For like command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.like();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For next command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For prev command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For createPlaylist command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.createPlaylist(commandInput
                .getPlaylistName(), commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For addRemoveInPlaylist command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For switchVisibility command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For showPlaylists command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     * For follow command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }
    /**
     * For status command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }
    /**
     * For showLikedSongs command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
    /**
     * For getPreferredGenre command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }
    /**
     * For getTop5Songs command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getInstance().getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }
    /**
     * For getTop5Playlists command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getInstance().getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }
    /**
     * For switchConnectionStatus command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = new String();
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (user.getType() == null || user.getType().equals("user")) {
            message = user.switchConnectionStatus();
        } else {
            message = user.getUsername() + " is not a normal user.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * For getOnlineUsers command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = Admin.getInstance().getOnlineUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(onlineUsers));
        return objectNode;
    }
    /**
     * For addUser command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String message = Admin.getInstance().addUser(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * For addAlbum command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (Admin.getUser(commandInput.getUsername()) != null) {
            message = Admin.getUser(commandInput.getUsername()).addAlbum(commandInput.getName(),
                    commandInput.getReleaseYear(), commandInput.getDescription(),
                    commandInput.getSongs(), Admin.getInstance().getAlbumsCount());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * For changePage command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        String message = Admin.getUser(commandInput.getUsername())
                .changePage(commandInput.getNextPage());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For printCurrentPage command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        String message = Admin.getUser(commandInput.getUsername()).printCurrentPage();
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (!Admin.getUser(commandInput.getUsername()).isOnline()) {
            message = Admin.getUser(commandInput.getUsername()).getUsername() + " is offline.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", message);
        return objectNode;
    }
    /**
     * For showAlbums command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<AlbumOutput> albumOutputs = ((Artist) user).showAlbums();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("result",  objectMapper.valueToTree(albumOutputs));
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For showPodcasts command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PodcastOutput> podcastOutputs = ((Host) user).showPodcasts();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("result",  objectMapper.valueToTree(podcastOutputs));
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For addEvent command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (Admin.getUser(commandInput.getUsername()) != null) {
            message = Admin.getUser(commandInput.getUsername()).addEvent(commandInput.getName(),
                    commandInput.getDescription(), commandInput.getDate());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For addMerch command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (Admin.getUser(commandInput.getUsername()) != null) {
            message = Admin.getUser(commandInput.getUsername())
                    .addMerchandise(commandInput.getName(),
                    commandInput.getDescription(), commandInput.getPrice());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For getAllUsers command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        List<String> getAllUsers = Admin.getInstance().getAllUsers();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("result", objectMapper.valueToTree(getAllUsers));
        objectNode.put("timestamp", commandInput.getTimestamp());
        return objectNode;
    }
    /**
     * For deleteUser command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String message = Admin.getInstance().removeUser(commandInput);
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For addPodcast command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user != null) {
            message = user.addPodcast(commandInput.getName(), commandInput.getEpisodes());
        } else {
            message = "The username " + user.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For addAnnouncement command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (Admin.getUser(commandInput.getUsername()) != null) {
            message = Admin.getUser(commandInput.getUsername())
                    .addAnnouncement(commandInput.getName(),
                            commandInput.getDescription());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For removeAnnouncement command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (Admin.getUser(commandInput.getUsername()) != null) {
            message = user.removeAnnouncement(commandInput.getName());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For removeAllbum command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Artist artist = null;
        if (user != null) {
            if (user.getType() != null) {
                if (user.getType().equals("artist")) {
                    artist = (Artist) user;
                }
            }
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (Admin.getUser(commandInput.getUsername()) != null && artist != null) {
            message = artist.removeAlbum(commandInput.getName());
        } else if (user == null)  {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (artist == null) {
            message = commandInput.getUsername() + " is not an artist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For removePodcast command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Host host = null;
        if (user != null) {
            if (user.getType() != null) {
                if (user.getType().equals("host")) {
                    host = (Host) user;
                }
            }
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (Admin.getUser(commandInput.getUsername()) != null && host != null) {
            message = host.removePodcast(commandInput.getName());
        } else if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (host == null) {
            message = commandInput.getUsername() + " is not a host.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For removeEvent command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Artist artist = null;
        if (user != null) {
            if (user.getType() != null) {
                if (user.getType().equals("artist")) {
                    artist = (Artist) user;
                }
            }
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = new String();
        if (artist != null) {
            message = artist.removeEvent(commandInput.getName());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("message", message);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        return objectNode;
    }
    /**
     * For getTop5Albums command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Admin.getInstance().getTop5Albums();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }
    /**
     * For getTop5Artists command
     * @param commandInput the given command
     * @return a json node
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        List<String> artists = Admin.getInstance().getTop5Artists();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(artists));
        return objectNode;
    }

    public static ObjectNode wrapped(CommandInput command) {
        JsonNode stats = Admin.getInstance().wrapped(command);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("user", command.getUsername());
        objectNode.put("timestamp", command.getTimestamp());
        String message = null;
        if (Admin.getUser(command.getUsername()).getType().equals("artist") && stats == null) {
            message = "No data to show for artist " + command.getUsername() + ".";
            objectNode.put("message", message);
        }
        if (stats != null) {
            objectNode.set("result", stats);
        } else if (message == null) {
            message = "No data to show for user " + command.getUsername() + ".";
            objectNode.put("message", message);
        }
        return objectNode;
    }

    public static ObjectNode endProgram () {
        JsonNode result = Admin.getInstance().endProgram();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "endProgram");
        objectNode.put("result", result);
        return objectNode;
    }

    public static ObjectNode buyPremium(CommandInput command) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("user", command.getUsername());
        objectNode.put("timestamp", command.getTimestamp());
        User user = Admin.getUser(command.getUsername());
        String message = null;
        if (user == null) {
            message = "The username " + command.getUsername() + " doesn't exist.";
        } else if (user.getType() == null || user.getType().equals("user")) {
            if (user.getCredits() == 0) {
                message = user.getUsername() + " bought the subscription successfully.";
                user.buyPremium();
            } else {
                message = user.getUsername() + " is already a premium user.";
            }
        }
        objectNode.put("message", message);
        return  objectNode;
    }
    public static ObjectNode cancelPremium(CommandInput command) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("user", command.getUsername());
        objectNode.put("timestamp", command.getTimestamp());
        User user = Admin.getUser(command.getUsername());
        String message = null;
        if (user == null) {
            message = "The username " + command.getUsername() + " doesn't exist.";
        } else if (user.getType() == null || user.getType().equals("user")) {
            if (user.getCredits() != 0) {
                message = user.getUsername() + " cancelled the subscription successfully.";
                user.cancelPremium();
            } else {
                message = user.getUsername() + " is not a premium user.";
            }
        }
        objectNode.put("message", message);
        return  objectNode;
    }
    public static ObjectNode adBreak (CommandInput command, LibraryInput library) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("user", command.getUsername());
        objectNode.put("timestamp", command.getTimestamp());
        User user = Admin.getInstance().getUser(command.getUsername());
        SongInput adBreak = library.getSongs().getFirst();
        Song ad = new Song(adBreak.getName(), adBreak.getDuration(), adBreak.getAlbum() ,
                    adBreak.getTags(),adBreak.getLyrics(),adBreak.getGenre(),
                    adBreak.getReleaseYear(), adBreak.getArtist());
        String message = null;
        if (user == null) {
            message = "The username " + command.getUsername() + " doesn't exist.";
        } else if (user.getType() == null || user.getType().equals("user")) {
            if (user.getPlayer().getSource() == null){
                message = user.getUsername() + " is not playing any music.";
            } else if (!user.getPlayer().getSource().getAudioFile().getType().equals("song")) {
                message = user.getUsername() + " is not playing any music.";
            } else {
                message = "Ad inserted successfully.";
                user.adBreak(command.getTimestamp(), ad);
            }
        }
        objectNode.put("message", message);
        return  objectNode;
    }
    public static ObjectNode subscribe (CommandInput command) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", command.getCommand());
        objectNode.put("user", command.getUsername());
        objectNode.put("timestamp", command.getTimestamp());
        User user = Admin.getInstance().getUser(command.getUsername());
        String message = null;
        if (user == null) {
            message = "The username " + command.getUsername() + " doesn't exist.";
        } else if (user.getType() == null || user.getType().equals("user")) {
            if (!user.getNextPage().type().equals("ArtistPage")
                    && !user.getNextPage().type().equals("HostPage")) {
                message = "To subscribe you need to be on the page of an artist or host.";
                objectNode.put("message", message);
                return objectNode;
            }
            User pageOwner = null;
            if (user.getNextPage().type().equals("ArtistPage")) {
                pageOwner = ((ArtistPage) user.getNextPage()).getUser();
            } else {
                pageOwner = ((HostPage) user.getNextPage()).getUser();
            }
            SubscribeFunction subscribeFunction = new SubscribeFunction(user, pageOwner);
            message = subscribeFunction.execute();
            objectNode.put("message", message);
        }
        return objectNode;
    }

    }
