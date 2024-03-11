package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.SingleFieldSpec;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticCartesianCentroidAggregation extends ElasticAggregations {

    @JsonProperty("cartesian_centroid")
    private final SingleFieldSpec cartesianCentroid;

    ElasticCartesianCentroidAggregation(
        SingleFieldSpec cartesianCentroid
    ) {
        this.cartesianCentroid = cartesianCentroid;
    }

    public static ElasticCartesianCentroidAggregation cartesianCentroid(String field) {
        return new ElasticCartesianCentroidAggregation(new SingleFieldSpec(field));
    }

}
