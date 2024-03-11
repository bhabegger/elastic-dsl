package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticStringStatsAggregation extends ElasticAggregations {

    @JsonProperty("string_stats")
    private final StringStatsBody stringStats;

    ElasticStringStatsAggregation(
        StringStatsBody stringStats
    ) {
        this.stringStats = stringStats;
    }
    public static ElasticStringStatsAggregation stringStats(String field) {
        return new ElasticStringStatsAggregation(new StringStatsBody(field, null, null));
    }

    public ElasticStringStatsAggregation withShowDistribution() {
        return withBody(original -> new StringStatsBody(
            original.field,
            true,
            original.missing
        ));
    }

    public ElasticStringStatsAggregation withMissing(String missing) {
        return withBody(original -> new StringStatsBody(
            original.field,
            original.showDistribution,
            missing
        ));
    }

    private ElasticStringStatsAggregation withBody(Function<StringStatsBody, StringStatsBody> update) {
        return new ElasticStringStatsAggregation(update.apply(this.stringStats));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record StringStatsBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("show_distribution")
        Boolean showDistribution,
        @JsonProperty("missing")
        String missing
    ) {}
}
