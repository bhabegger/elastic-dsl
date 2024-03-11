package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticSumAggregation extends ElasticAggregations {

    @JsonProperty("sum")
    private final StatsBody sum;

    ElasticSumAggregation(
            StatsBody sum
    ) {
        this.sum = sum;
    }
    public static ElasticSumAggregation sum(String field) {
        return new ElasticSumAggregation(new StatsBody(field, null));
    }

    public static ElasticSumAggregation sum(String field, Integer missing) {
        return new ElasticSumAggregation(new StatsBody(field, missing));
    }
}
