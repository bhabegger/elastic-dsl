package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticAvgAggregation extends ElasticAggregations {

    @JsonProperty("avg")
    private final StatsBody avg;

    ElasticAvgAggregation(
        StatsBody avg
    ) {
        this.avg = avg;
    }

    public static ElasticAggregations avg(String field) {
        return new ElasticAvgAggregation(new StatsBody(field, null));
    }

    public static ElasticAggregations avg(String field, Integer missing) {
        return new ElasticAvgAggregation(new StatsBody(field, missing));
    }
}
