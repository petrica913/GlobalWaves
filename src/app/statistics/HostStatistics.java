package app.statistics;

import app.user.Host;
public class HostStatistics implements Statistics{
    private final Host host;
    public HostStatistics(Host host) {
        this.host = host;
    }

    @Override
    public String generateStatistics() {
//        implement the logic for generating host stats
        return null;
    }
}
