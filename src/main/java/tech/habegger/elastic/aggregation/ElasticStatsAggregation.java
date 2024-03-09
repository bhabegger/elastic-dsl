package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticStatsAggregation extends ElasticAggregations {

    @JsonProperty("stats")
    private final StatsBody stats;

    ElasticStatsAggregation(
            StatsBody stats,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.stats = stats;
    }

    public static ElasticAggregations statsAggregation(String field) {
        return new ElasticStatsAggregation(new StatsBody(field), null);
    }

    private record StatsBody(String script) {
    }
}
