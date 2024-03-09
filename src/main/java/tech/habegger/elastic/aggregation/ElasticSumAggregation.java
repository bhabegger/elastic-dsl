package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticSumAggregation extends ElasticAggregations {

    @JsonProperty("sum")
    private final SumBody sum;

    ElasticSumAggregation(
            SumBody sum,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.sum = sum;
    }

    public static ElasticAggregations sumAggregation(String field) {
        return new ElasticSumAggregation(new SumBody(field), null);
    }

    private record SumBody(String field) {
    }
}
