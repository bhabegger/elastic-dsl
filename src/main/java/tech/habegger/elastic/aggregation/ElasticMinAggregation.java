package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticMinAggregation extends ElasticAggregations {

    @JsonProperty("min")
    private final StatsBody min;

    ElasticMinAggregation(
        StatsBody min
    ) {
        this.min = min;
    }

    public static ElasticAggregations min(String field) {
        return new ElasticMinAggregation(new StatsBody(field, null));
    }
    public static ElasticAggregations min(String field, Integer missing) {
        return new ElasticMinAggregation(new StatsBody(field, missing));
    }
}
