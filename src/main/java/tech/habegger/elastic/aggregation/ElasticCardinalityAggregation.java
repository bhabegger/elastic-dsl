package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticCardinalityAggregation extends ElasticAggregations {

    @JsonProperty("cardinality")
    private final CardinalityBody cardinality;

    ElasticCardinalityAggregation(
        CardinalityBody cardinality
    ) {
        this.cardinality = cardinality;
    }
    public static ElasticCardinalityAggregation cardinality(String field) {
        return new ElasticCardinalityAggregation(new CardinalityBody(field, null, null, null));
    }

    public ElasticCardinalityAggregation withPrecisionThreshold(int precisionThreshold) {
        return withBody(original -> new CardinalityBody(
            original.field,
            precisionThreshold,
            original.executionHint,
            original.missing
        ));
    }

    public ElasticCardinalityAggregation withExecutionHint(CardinalityExecutionHint executionHint) {
        return withBody(original -> new CardinalityBody(
            original.field,
            original.precisionThreshold,
            executionHint,
            original.missing
        ));
    }

    public ElasticCardinalityAggregation withMissing(String missing) {
        return withBody(original -> new CardinalityBody(
            original.field,
            original.precisionThreshold,
            original.executionHint,
            missing
        ));
    }

    private ElasticCardinalityAggregation withBody(Function<CardinalityBody, CardinalityBody> update) {
        return new ElasticCardinalityAggregation(update.apply(this.cardinality));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record CardinalityBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("precision_threshold")
        Integer precisionThreshold,
        @JsonProperty("execution_hint")
        CardinalityExecutionHint executionHint,
        @JsonProperty("missing")
        String missing
    ) {}

    @SuppressWarnings("unused")
    public enum CardinalityExecutionHint {
        direct,
        global_ordinals,
        segment_ordinals,
        save_time_heuristic,
        save_memory_heuristic
    }
}
