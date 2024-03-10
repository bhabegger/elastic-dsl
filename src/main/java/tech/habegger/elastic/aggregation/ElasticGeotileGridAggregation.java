package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.GeoRect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeotileGridAggregation extends AbstractElasticGeoGridAggregation {

    @JsonProperty("geotile_grid")
    final GeoGridAggregationBody geohashGridBody;

    ElasticGeotileGridAggregation(
        GeoGridAggregationBody geohashGridBody
    ) {
        this.geohashGridBody = geohashGridBody;
    }

    public ElasticGeotileGridAggregation withBounds(GeoRect bounds) {
        return new ElasticGeotileGridAggregation(geohashGridBody.withBounds(bounds));
    }
    public ElasticGeotileGridAggregation withSize(Integer size) {
        return new ElasticGeotileGridAggregation(geohashGridBody.withSize(size));
    }

    public ElasticGeotileGridAggregation withShardSize(Integer size) {
        return new ElasticGeotileGridAggregation(geohashGridBody.withShardSize(size));
    }

    public static ElasticGeotileGridAggregation geotileGrid(String field, Integer precision) {
        return new ElasticGeotileGridAggregation(new GeoGridAggregationBody(field, precision, null, null, null));
    }
}
