package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticStatsAggregation extends ElasticAggregations {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElasticStatsAggregation) obj;
        return Objects.equals(this.stats, that.stats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stats);
    }

    private record StatsBody(String script) {
    }
}
