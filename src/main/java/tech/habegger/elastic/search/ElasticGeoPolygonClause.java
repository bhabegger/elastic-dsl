package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticGeoPolygonClause implements ElasticSearchClause {
    @JsonProperty("geo_polygon")
    private final Map<String, GeoPolygonBody> geoPolygon;

    ElasticGeoPolygonClause(Map<String, GeoPolygonBody> geoPolygon) {
        this.geoPolygon = geoPolygon;
    }
    public static ElasticGeoPolygonClause geoPolygon(String field, GeoCoord... points) {
        return new ElasticGeoPolygonClause(Map.of(field, new GeoPolygonBody(Arrays.asList(points))));
    }

    record GeoPolygonBody(
            List<GeoCoord> points
    ) {}
}
