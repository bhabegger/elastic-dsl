package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticValueCountAggregation extends ElasticAggregations {

    @JsonProperty("value_count")
    private final StatsBody valueCount;

    ElasticValueCountAggregation(
        StatsBody valueCount
    ) {
        this.valueCount = valueCount;
    }

    public static ElasticValueCountAggregation valueCount(String field) {
        return new ElasticValueCountAggregation(new StatsBody(field, null));
    }
    public static ElasticValueCountAggregation valueCount(String field, Integer missing) {
        return new ElasticValueCountAggregation(new StatsBody(field, missing));
    }
}
