package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.DistanceType;
import tech.habegger.elastic.shared.DistanceUnit;
import tech.habegger.elastic.shared.GeoCoord;
import tech.habegger.elastic.shared.Range;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeoDistanceAggregation extends ElasticAggregations {
    @JsonProperty("geo_distance")
    private final GeoDistanceBody geoDistance;

    ElasticGeoDistanceAggregation(
        @JsonProperty("geo_distance")
        GeoDistanceBody geoDistance

    ) {
        this.geoDistance = geoDistance;
    }

    public ElasticGeoDistanceAggregation withDistanceType(DistanceType distanceType) {
        return withBody((original) ->
            new GeoDistanceBody(
                original.field,
                original.origin,
                original.ranges,
                distanceType,
                original.unit,
                original.keyed
            ));
    }

    public ElasticGeoDistanceAggregation withUnit(DistanceUnit unit) {
        return withBody((original) ->
            new GeoDistanceBody(
                original.field,
                original.origin,
                original.ranges,
                original.distanceType,
                unit,
                original.keyed
            ));
    }
    public ElasticGeoDistanceAggregation withKeyed() {
        return withBody((original) ->
            new GeoDistanceBody(
                original.field,
                original.origin,
                original.ranges,
                original.distanceType,
                original.unit,
                true
            ));
    }

    private ElasticGeoDistanceAggregation withBody(Function<GeoDistanceBody, GeoDistanceBody> update) {
        return new ElasticGeoDistanceAggregation(update.apply(this.geoDistance));
    }

    public static ElasticGeoDistanceAggregation geoDistance(String field, GeoCoord origin, Range... ranges) {
        return new ElasticGeoDistanceAggregation(
            new GeoDistanceBody(field, origin, Arrays.asList(ranges), null, null, null)
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record GeoDistanceBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("origin")
        GeoCoord origin,
        @JsonProperty("ranges")
        List<Range> ranges,
        @JsonProperty("distance_type")
        DistanceType distanceType,
        @JsonProperty("unit")
        DistanceUnit unit,
        @JsonProperty("keyed")
        Boolean keyed
    ) {
    }

}
