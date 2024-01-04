package app.statistics;

import app.user.Host;
import com.fasterxml.jackson.databind.JsonNode;

public class HostStatistics implements Statistics{
    private final Host host;
    public HostStatistics(Host host) {
        this.host = host;
    }

    @Override
    public JsonNode generateStatistics() {
//        implement the logic for generating host stats
        return null;
    }
}
