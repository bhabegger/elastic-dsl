package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.GeoRect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeohexGridAggregation extends AbstractElasticGeoGridAggregation {

    @JsonProperty("geohex_grid")
    final GeoGridAggregationBody geohashGridBody;

    ElasticGeohexGridAggregation(
        GeoGridAggregationBody geohashGridBody
    ) {
        this.geohashGridBody = geohashGridBody;
    }

    public ElasticGeohexGridAggregation withBounds(GeoRect bounds) {
        return new ElasticGeohexGridAggregation(geohashGridBody.withBounds(bounds));
    }
    public ElasticGeohexGridAggregation withSize(Integer size) {
        return new ElasticGeohexGridAggregation(geohashGridBody.withSize(size));
    }

    public ElasticGeohexGridAggregation withShardSize(Integer size) {
        return new ElasticGeohexGridAggregation(geohashGridBody.withShardSize(size));
    }

    public static ElasticGeohexGridAggregation geohexGrid(String field, Integer precision) {
        return new ElasticGeohexGridAggregation(new GeoGridAggregationBody(field, precision, null, null, null));
    }
}
