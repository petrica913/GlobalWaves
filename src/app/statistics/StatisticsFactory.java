package app.statistics;

import app.user.Artist;
import app.user.Host;
import app.user.User;

public interface StatisticsFactory {
    /**
     * @param user for the given user
     * @return the creation of user stats
     */
    Statistics createUserStatistics(User user);
    /**
     * @param user for the given user
     * @return the creation of host stats
     */
    Statistics createHostStatistics(Host user);
    /**
     * @param user for the given user
     * @return the creation of artist stats
     */
    Statistics createArtistStatistics(Artist user);
}
