package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticWeightedAvgAggregation extends ElasticAggregations {

    @JsonProperty("weighted_avg")
    private final WeightedAvgBody weightedAvg;

    ElasticWeightedAvgAggregation(
        WeightedAvgBody weightedAvg
    ) {
        this.weightedAvg = weightedAvg;
    }
    public static ElasticWeightedAvgAggregation weightedAvg(String value, String weight) {
        return new ElasticWeightedAvgAggregation(new WeightedAvgBody(
            new FieldSpec(value, null),
            new FieldSpec(weight, null)
        ));
    }

    public static ElasticWeightedAvgAggregation weightedAvg(FieldSpec value, FieldSpec weight) {
        return new ElasticWeightedAvgAggregation(new WeightedAvgBody(value, weight));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record WeightedAvgBody(
        @JsonProperty("value")
        FieldSpec value,
        @JsonProperty("weight")
        FieldSpec weight
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FieldSpec(String field, Double missing) {
        public FieldSpec withMissing(Double missing) {
            return new FieldSpec(field, missing);
        }
    }

    public static FieldSpec fieldSpec(String field) {
        return new FieldSpec(field, null);
    }
}
