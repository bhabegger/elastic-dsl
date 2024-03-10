package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticMaxAggregation extends ElasticAggregations {

    @JsonProperty("max")
    private final StatsBody max;

    ElasticMaxAggregation(
        StatsBody max
    ) {
        this.max = max;
    }

    public static ElasticAggregations max(String field) {
        return new ElasticMaxAggregation(new StatsBody(field, null));
    }
    public static ElasticAggregations max(String field, Integer missing) {
        return new ElasticMaxAggregation(new StatsBody(field, missing));
    }
}
