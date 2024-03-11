package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.HDRSpec;
import tech.habegger.elastic.shared.TDigestSpec;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticPercentilesAggregation extends ElasticAggregations {
    @JsonProperty("percentiles")
    private final PercentilesBody percentiles;

    ElasticPercentilesAggregation(
        @JsonProperty("percentile_ranks")
        PercentilesBody percentiles

    ) {
        this.percentiles = percentiles;
    }
    public ElasticAggregations withTDigest(int compression, TDigestSpec.ExecutionHint executionHint) {
        return withBody((original) ->
            new PercentilesBody(
                original.field,
                original.percents,
                new TDigestSpec(compression, executionHint),
                original.hdr,
                original.keyed,
                original.missing
            ));
    }
    public ElasticAggregations withTDigest(int compression) {
        return withBody((original) ->
            new PercentilesBody(
                original.field,
                original.percents,
                new TDigestSpec(compression, null),
                original.hdr,
                original.keyed,
                original.missing
            ));
    }
    public ElasticAggregations withTDigest(TDigestSpec.ExecutionHint executionHint) {
        return withBody((original) ->
            new PercentilesBody(
                original.field,
                original.percents,
                new TDigestSpec(null, executionHint),
                original.hdr,
                original.keyed,
                original.missing
            ));
    }
    public ElasticAggregations withHdr(int numberOfSignificantValueDigits) {
        return withBody((original) ->
                new PercentilesBody(
                    original.field,
                    original.percents,
                    original.tdigest,
                    new HDRSpec(numberOfSignificantValueDigits),
                    original.keyed,
                    original.missing
                ));
    }
    public ElasticAggregations withKeyed() {
        return withBody((original) ->
            new PercentilesBody(
                original.field,
                original.percents,
                original.tdigest,
                original.hdr,
                true,
                original.missing
            ));
    }

    public ElasticAggregations withMissing(String missing) {
        return withBody((original) ->
                new PercentilesBody(
                    original.field,
                    original.percents,
                    original.tdigest,
                    original.hdr,
                    original.keyed,
                    missing
                ));
    }
    private ElasticPercentilesAggregation withBody(Function<PercentilesBody, PercentilesBody> update) {
        return new ElasticPercentilesAggregation(update.apply(this.percentiles));
    }

    public static ElasticPercentilesAggregation percentiles(String field) {
        return new ElasticPercentilesAggregation(
            new PercentilesBody(field, null,  null, null, null, null)
        );
    }

    public static ElasticPercentilesAggregation percentiles(String field, Double... percents) {
        return new ElasticPercentilesAggregation(
            new PercentilesBody(field, Arrays.asList(percents),  null, null, null, null)
        );
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record PercentilesBody(
            @JsonProperty("field")
            String field,
            @JsonProperty("percents")
            List<Double> percents,
            @JsonProperty("tdigest")
            TDigestSpec tdigest,
            @JsonProperty("hdr")
            HDRSpec hdr,
            @JsonProperty("keyed")
            Boolean keyed,
            @JsonProperty("missing")
            String missing
    ) { }

}
