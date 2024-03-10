package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.GeoRect;

import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeohashGridAggregation extends ElasticAggregations {

    @JsonProperty("geohash_grid")
    final GeohashGridBody geohashGridBody;

    ElasticGeohashGridAggregation(
        GeohashGridBody geohash_grid
    ) {
        this.geohashGridBody = geohash_grid;
    }

    public ElasticGeohashGridAggregation withBounds(GeoRect bounds) {
        return withBody(original -> new GeohashGridBody(
            original.field,
            original.precision,
            bounds,
            original.size,
            original.shardSize
        ));
    }
    public ElasticGeohashGridAggregation withSize(Integer size) {
        return withBody(original -> new GeohashGridBody(
            original.field,
            original.precision,
            original.bounds,
            size,
            original.shardSize
        ));
    }

    public ElasticGeohashGridAggregation withShardSize(Integer shardSize) {
        return withBody(original -> new GeohashGridBody(
            original.field,
            original.precision,
            original.bounds,
            original.size,
            shardSize
        ));
    }

    private ElasticGeohashGridAggregation withBody(Function<GeohashGridBody, GeohashGridBody> update) {
        return new ElasticGeohashGridAggregation(update.apply(this.geohashGridBody));
    }

    public static ElasticGeohashGridAggregation geohashGrid(String field, Integer precision) {
        return new ElasticGeohashGridAggregation(new GeohashGridBody(field, precision, null, null, null));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record GeohashGridBody(
        String field,
        Integer precision,
        GeoRect bounds,
        Integer size,
        @JsonProperty("shard_size")
        Integer shardSize
    ) {
    }
}
