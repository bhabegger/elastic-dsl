package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.FieldInstanceReference;
import tech.habegger.elastic.shared.GeoCoord;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticGeoShapeClause(
        @JsonProperty("geo_shape")
        Map<String, GeoShapeBody> geoShape
) implements ElasticSearchClause {

    public static ElasticGeoShapeClause geoShape(String field, GeoInlineShapeDefinition shape) {
        return geoShape(field, null, shape);
    }

    public static ElasticGeoShapeClause geoShape(String field, GeoShapeRelation relation, GeoInlineShapeDefinition shape) {
        return new ElasticGeoShapeClause(Map.of(field, new GeoShapeBody(shape, null, relation)));
    }

    public static ElasticGeoShapeClause geoShape(String field, FieldInstanceReference shape) {
        return geoShape(field, null, shape);
    }
        public static ElasticGeoShapeClause geoShape(String field, GeoShapeRelation relation, FieldInstanceReference shape) {
        return new ElasticGeoShapeClause(Map.of(field, new GeoShapeBody(null, shape, relation)));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record GeoShapeBody(
            GeoInlineShapeDefinition shape,
            @JsonProperty("indexed_shape")
            FieldInstanceReference indexedShape,
            GeoShapeRelation relation
    ) {}
    public record GeoInlineShapeDefinition(String type, List<List<Float>> coordinates) {
        public static GeoInlineShapeDefinition geoEnvelop(GeoCoord topLeft, GeoCoord bottomRight) {
            return new GeoInlineShapeDefinition(
                    "envelope",
                    List.of(
                        List.of(topLeft.lat(), topLeft.lon()),
                        List.of(bottomRight.lat(), bottomRight.lon())
                    )
            );
        }
        public static GeoInlineShapeDefinition geoPolygon(GeoCoord...coordinates) {
            return new GeoInlineShapeDefinition(
                    "envelope",
                    Arrays.stream(coordinates).map(c -> List.of(c.lat(), c.lon())).collect(Collectors.toList())
            );
        }
    }
    public enum GeoShapeRelation {
        intersects,
        disjoint,
        within,
        contains
    }
}
