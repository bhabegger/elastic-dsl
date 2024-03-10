package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.GeoRect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeohashGridAggregation extends AbstractElasticGeoGridAggregation {

    @JsonProperty("geohash_grid")
    final GeoGridAggregationBody geohashGridBody;

    ElasticGeohashGridAggregation(
        GeoGridAggregationBody geohashGridBody
    ) {
        this.geohashGridBody = geohashGridBody;
    }

    public ElasticGeohashGridAggregation withBounds(GeoRect bounds) {
        return new ElasticGeohashGridAggregation(geohashGridBody.withBounds(bounds));
    }
    public ElasticGeohashGridAggregation withSize(Integer size) {
        return new ElasticGeohashGridAggregation(geohashGridBody.withSize(size));
    }

    public ElasticGeohashGridAggregation withShardSize(Integer size) {
        return new ElasticGeohashGridAggregation(geohashGridBody.withShardSize(size));
    }

    public static ElasticGeohashGridAggregation geohashGrid(String field, Integer precision) {
        return new ElasticGeohashGridAggregation(new GeoGridAggregationBody(field, precision, null, null, null));
    }
}
