package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticMaxAggregation extends ElasticAggregations {

    @JsonProperty("max")
    private final MaxBody max;

    ElasticMaxAggregation(
            MaxBody max,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.max = max;
    }

    public static ElasticAggregations maxAggregation(String field) {
        return new ElasticMaxAggregation(new MaxBody(field), null);
    }
    private record MaxBody(String field) {
    }
}
