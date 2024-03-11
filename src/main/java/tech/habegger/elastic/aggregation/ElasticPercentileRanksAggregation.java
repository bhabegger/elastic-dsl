package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.HDRSpec;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticPercentileRanksAggregation extends ElasticAggregations {
    @JsonProperty("percentile_ranks")
    private final PercentileRankBody percentileRanks;

    ElasticPercentileRanksAggregation(
        @JsonProperty("percentile_ranks")
        PercentileRankBody percentileRanks

    ) {
        this.percentileRanks = percentileRanks;
    }
    public ElasticAggregations withHdr(int numberOfSignificantValueDigits) {
        return withBody((original) ->
                new PercentileRankBody(
                    original.field,
                    original.values,
                    new HDRSpec(numberOfSignificantValueDigits),
                    original.keyed,
                    original.missing
                ));
    }
    public ElasticAggregations withKeyed() {
        return withBody((original) ->
            new PercentileRankBody(
                original.field,
                original.values,
                original.hdr,
                true,
                original.missing
            ));
    }

    public ElasticAggregations withMissing(String missing) {
        return withBody((original) ->
                new PercentileRankBody(
                    original.field,
                    original.values,
                    original.hdr,
                    original.keyed,
                    missing
                ));
    }
    private ElasticPercentileRanksAggregation withBody(Function<PercentileRankBody, PercentileRankBody> update) {
        return new ElasticPercentileRanksAggregation(update.apply(this.percentileRanks));
    }

    public static ElasticPercentileRanksAggregation percentileRanks(String field, Double... values) {
        return new ElasticPercentileRanksAggregation(
            new PercentileRankBody(field, Arrays.asList(values),  null, null, null)
        );
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record PercentileRankBody(
            @JsonProperty("field")
            String field,
            @JsonProperty("values")
            List<Double> values,
            @JsonProperty("hdr")
            HDRSpec hdr,
            @JsonProperty("keyed")
            Boolean keyed,
            @JsonProperty("missing")
            String missing
    ) { }

}
