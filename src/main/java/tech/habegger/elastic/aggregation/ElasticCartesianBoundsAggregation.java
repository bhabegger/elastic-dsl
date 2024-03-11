package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.SingleFieldSpec;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticCartesianBoundsAggregation extends ElasticAggregations {

    @JsonProperty("cartesian_bounds")
    private final SingleFieldSpec cartesianBounds;

    ElasticCartesianBoundsAggregation(
        SingleFieldSpec cartesianBounds
    ) {
        this.cartesianBounds = cartesianBounds;
    }

    public static ElasticCartesianBoundsAggregation cartesianBounds(String field) {
        return new ElasticCartesianBoundsAggregation(new SingleFieldSpec(field));
    }

}
