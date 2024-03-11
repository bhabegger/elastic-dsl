package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticMedianAbsoluteDeviationAggregation extends ElasticAggregations {

    @JsonProperty("median_absolute_deviation")
    private final MedianAbsoluteDeviationBody medianAbsoluteDeviation;

    ElasticMedianAbsoluteDeviationAggregation(
        MedianAbsoluteDeviationBody medianAbsoluteDeviation
    ) {
        this.medianAbsoluteDeviation = medianAbsoluteDeviation;
    }
    public static ElasticMedianAbsoluteDeviationAggregation medianAbsoluteDeviation(String field) {
        return new ElasticMedianAbsoluteDeviationAggregation(new MedianAbsoluteDeviationBody(field, null, null));
    }

    public ElasticMedianAbsoluteDeviationAggregation withCompression(int compression) {
        return withBody(original -> new MedianAbsoluteDeviationBody(
            original.field,
            compression,
            original.missing
        ));
    }


    public ElasticMedianAbsoluteDeviationAggregation withMissing(float missing) {
        return withBody(original -> new MedianAbsoluteDeviationBody(
            original.field,
            original.compression,
            missing
        ));
    }

    private ElasticMedianAbsoluteDeviationAggregation withBody(Function<MedianAbsoluteDeviationBody, MedianAbsoluteDeviationBody> update) {
        return new ElasticMedianAbsoluteDeviationAggregation(update.apply(this.medianAbsoluteDeviation));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record MedianAbsoluteDeviationBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("compression")
        Integer compression,
        @JsonProperty("missing")
        Float missing
    ) {}
}
