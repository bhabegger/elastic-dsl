package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.RateUnit;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticIpRateAggregation extends ElasticAggregations {
    @JsonProperty("rate")
    private final RateBody rate;

    ElasticIpRateAggregation(
        @JsonProperty("rate")
        RateBody rate

    ) {
        this.rate = rate;
    }

    public static ElasticIpRateAggregation rate(RateUnit rate) {
        return new ElasticIpRateAggregation(
            new RateBody(null, rate, null)
        );
    }
    public static ElasticIpRateAggregation rate(RateUnit rate, RateMode mode) {
        return new ElasticIpRateAggregation(
            new RateBody(null, rate, mode)
        );
    }

    public static ElasticIpRateAggregation rate(String field, RateUnit rate) {
        return new ElasticIpRateAggregation(
            new RateBody(field, rate, null)
        );
    }

    public static ElasticIpRateAggregation rate(String field, RateUnit rate, RateMode mode) {
        return new ElasticIpRateAggregation(
            new RateBody(field, rate, mode)
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record RateBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("unit")
        RateUnit unit,
        @JsonProperty("mode")
        RateMode mode
    ) {
    }

    public enum RateMode {
        sum,
        value_count
    }
}
