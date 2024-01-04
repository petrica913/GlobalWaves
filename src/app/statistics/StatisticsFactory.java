package app.statistics;

import app.user.Artist;
import app.user.Host;
import app.user.User;

public interface StatisticsFactory {
    Statistics createUserStatistics(User user);
    Statistics createHostStatistics(Host user);
    Statistics createArtistStatistics(Artist user);
}
