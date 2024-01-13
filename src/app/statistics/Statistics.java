package app.statistics;

import com.fasterxml.jackson.databind.JsonNode;

public interface Statistics {
    /**
     *  It is used for generating the stats
     */
    JsonNode generateStatistics();
}
