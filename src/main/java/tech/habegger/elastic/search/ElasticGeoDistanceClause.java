package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import tech.habegger.elastic.shared.GeoCoord;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticGeoDistanceClause implements ElasticSearchClause {
    @JsonProperty("geo_distance")
    @JsonPropertyOrder(alphabetic = true)
    private final Map<String, Object> geoDistance;

    ElasticGeoDistanceClause(
            Map<String, Object> geoDistance
    ) {
        this.geoDistance = geoDistance;
    }
    public static ElasticGeoDistanceClause geoDistance(String field, String distance, GeoCoord point) {
        return new ElasticGeoDistanceClause(
                Map.of(
                "distance", distance,
                    field, point
                )
        );
    }
}
