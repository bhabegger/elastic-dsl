package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticDistanceFeatureClause(
        @JsonProperty("distance_feature")
        DistanceFeatureBody distanceFeature
) implements ElasticSearchClause {

    public static ElasticDistanceFeatureClause distanceFeature(String field, String pivot, String origin) {
        return new ElasticDistanceFeatureClause(
                new DistanceFeatureBody(field, pivot, origin)
        );
    }
    public static ElasticDistanceFeatureClause distanceFeature(String field, String pivot, GeoCoord origin) {
        return new ElasticDistanceFeatureClause(
            new DistanceFeatureBody(field, pivot, List.of(origin.lat(), origin.lon()))
        );
    }

    record DistanceFeatureBody(
            String field,
            String pivot,
            Object origin
    ) {}
}
