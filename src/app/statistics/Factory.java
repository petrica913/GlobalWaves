package app.statistics;

import app.user.Artist;
import app.user.Host;
import app.user.User;

public class Factory implements StatisticsFactory {
    /**
     * @param user for the artist
     * @return the stats for the given artist
     */
    @Override
    public Statistics createArtistStatistics(final Artist user) {
        return new ArtistStatistics(user);
    }

    /**
     * @param user for the user
     * @return the stats for the given user
     */
    @Override
    public Statistics createUserStatistics(final User user) {
        return new UserStatistics(user);
    }

    /**
     * @param user for the host
     * @return the stats for the given host
     */
    @Override
    public Statistics createHostStatistics(final Host user) {
        return new HostStatistics(user);
    }
}
