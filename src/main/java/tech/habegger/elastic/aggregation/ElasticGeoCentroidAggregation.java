package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeoCentroidAggregation extends ElasticAggregations {

    @JsonProperty("geo_centroid")
    private final GeoCentroidBody geoCentroid;

    ElasticGeoCentroidAggregation(
        GeoCentroidBody geoCentroid
    ) {
        this.geoCentroid = geoCentroid;
    }


    public static ElasticGeoCentroidAggregation geoCentroid(String field) {
        return new ElasticGeoCentroidAggregation(new GeoCentroidBody(field));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record GeoCentroidBody(
        String field
    ) { }

}
