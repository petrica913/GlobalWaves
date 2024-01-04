package app.statistics;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public interface Statistics {
    JsonNode generateStatistics();
}
