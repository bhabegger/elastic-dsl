package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticGeoGridClause implements ElasticSearchClause {
    @JsonProperty("geo_grid")
    private final Map<String, GeoGridBody> geoGrid;

    ElasticGeoGridClause(Map<String, GeoGridBody> geoGrid) {
        this.geoGrid = geoGrid;
    }
    public static ElasticGeoGridClause geoGrid(String field, String geoHash) {
        return new ElasticGeoGridClause(Map.of(field, new GeoGridBody(geoHash)));
    }

    record GeoGridBody(String geohash){}
}
