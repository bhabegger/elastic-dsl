package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticIpRangeAggregation extends ElasticAggregations {
    @JsonProperty("ip_range")
    private final IpRangeBody ipRange;

    ElasticIpRangeAggregation(
        @JsonProperty("ip_range")
        IpRangeBody ipRange

    ) {
        this.ipRange = ipRange;
    }
    public ElasticIpRangeAggregation withKeyed() {
        return withBody((original) ->
            new IpRangeBody(
                original.field,
                original.ranges,
                true
            ));
    }

    private ElasticIpRangeAggregation withBody(Function<IpRangeBody, IpRangeBody> update) {
        return new ElasticIpRangeAggregation(update.apply(this.ipRange));
    }

    public static ElasticIpRangeAggregation ipRange(String field, IpRange... ranges) {
        return new ElasticIpRangeAggregation(
            new IpRangeBody(field, Arrays.asList(ranges), null)
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record IpRangeBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("ranges")
        List<IpRange> ranges,
        @JsonProperty("keyed")
        Boolean keyed
    ) {
    }

}
