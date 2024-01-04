package app.statistics;

import app.user.Artist;
import app.user.Host;
import app.user.User;

public class Factory implements StatisticsFactory{
    @Override
    public Statistics createArtistStatistics(Artist user) {
        return new ArtistStatistics(user);
    }

    @Override
    public Statistics createUserStatistics(User user) {
        return new UserStatistics(user);
    }

    @Override
    public Statistics createHostStatistics(Host user) {
        return new HostStatistics(user);
    }
}
