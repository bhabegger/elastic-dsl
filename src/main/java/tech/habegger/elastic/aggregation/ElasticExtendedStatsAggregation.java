package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticExtendedStatsAggregation extends ElasticAggregations {

    @JsonProperty("extended_stats")
    private final ExtendedStatsBody extendedStats;

    ElasticExtendedStatsAggregation(
        ExtendedStatsBody extendedStats
    ) {
        this.extendedStats = extendedStats;
    }

    public ElasticExtendedStatsAggregation withSigma(Double sigma) {
        return withBody(original -> new ExtendedStatsBody(
            original.field,
            sigma,
            original.missing
        ));
    }

    public ElasticExtendedStatsAggregation withMissing(Float missing) {
        return withBody(original -> new ExtendedStatsBody(
            original.field,
            original.sigma,
            missing
        ));
    }

    private ElasticExtendedStatsAggregation withBody(Function<ExtendedStatsBody, ExtendedStatsBody> update) {
        return new ElasticExtendedStatsAggregation(update.apply(this.extendedStats));
    }

    public static ElasticExtendedStatsAggregation extendedStats(String field) {
        return new ElasticExtendedStatsAggregation(new ExtendedStatsBody(field, null, null));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ExtendedStatsBody(String field, Double sigma, Float missing) {
    }

}
