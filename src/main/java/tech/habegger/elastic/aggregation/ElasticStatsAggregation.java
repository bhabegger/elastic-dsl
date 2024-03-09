package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticStatsAggregation extends ElasticAggregations {

    @JsonProperty("stats")
    private final StatsBody stats;

    ElasticStatsAggregation(
            StatsBody stats
    ) {
        this.stats = stats;
    }

    public static ElasticAggregations statsAggregation(String field) {
        return new ElasticStatsAggregation(new StatsBody(field));
    }

    private record StatsBody(String script) {
    }
}
