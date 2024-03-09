package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.GeoCoord;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticGeoBoundingBoxClause implements ElasticSearchClause {
    @JsonProperty("geo_bounding_box")
    private final Map<String, GeoBoundingBoxBody> geoBoundingBox;

    ElasticGeoBoundingBoxClause(Map<String, GeoBoundingBoxBody> geoBoundingBox) {
        this.geoBoundingBox = geoBoundingBox;
    }
    public static ElasticGeoBoundingBoxClause geoBoundingBox(String field, GeoCoord topLeft, GeoCoord bottomRight) {
        return new ElasticGeoBoundingBoxClause(Map.of(field, new GeoBoundingBoxBody(topLeft, bottomRight)));
    }

    record GeoBoundingBoxBody(
            @JsonProperty("top_left")
            GeoCoord topLeft,
            @JsonProperty("bottom_right")
            GeoCoord bottomRight){}
}
