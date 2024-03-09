package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeohashGridAggregation extends ElasticAggregations {
    final GeohashGridBody geohash_grid;

    ElasticGeohashGridAggregation(
        GeohashGridBody geohash_grid
    ) {
        this.geohash_grid = geohash_grid;
    }
    public static ElasticAggregations geohashGridAgg(String field, Integer precision) {
        return new ElasticGeohashGridAggregation(new GeohashGridBody(field, precision));
    }

    record GeohashGridBody(String field, Integer precision) {
    }
}
