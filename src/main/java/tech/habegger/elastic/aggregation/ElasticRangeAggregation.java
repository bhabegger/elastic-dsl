package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.Range;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticRangeAggregation extends ElasticAggregations {
    @JsonProperty("range")
    private final RangeBody ipRange;

    ElasticRangeAggregation(
        @JsonProperty("range")
        RangeBody ipRange

    ) {
        this.ipRange = ipRange;
    }
    public ElasticRangeAggregation withKeyed() {
        return withBody((original) ->
            new RangeBody(
                original.field,
                original.ranges,
                true
            ));
    }

    private ElasticRangeAggregation withBody(Function<RangeBody, RangeBody> update) {
        return new ElasticRangeAggregation(update.apply(this.ipRange));
    }

    public static ElasticRangeAggregation rangeAgg(String field, Range... ranges) {
        return new ElasticRangeAggregation(
            new RangeBody(field, Arrays.asList(ranges), null)
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record RangeBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("ranges")
        List<Range> ranges,
        @JsonProperty("keyed")
        Boolean keyed
    ) {
    }

}
