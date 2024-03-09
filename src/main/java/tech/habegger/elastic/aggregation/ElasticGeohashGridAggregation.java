package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticGeohashGridAggregation extends ElasticAggregations {
    final GeohashGridBody geohash_grid;

    ElasticGeohashGridAggregation(
        GeohashGridBody geohash_grid,
        Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.geohash_grid = geohash_grid;
    }

    public static ElasticAggregations geohashGridAgg(String field, Integer precision) {
        return geohashGridAgg(field, precision, null);
    }
    public static ElasticAggregations geohashGridAgg(String field, Integer precision, Map<String, ElasticAggregations> aggregations) {
        return new ElasticGeohashGridAggregation(new GeohashGridBody(field, precision), aggregations);
    }

    record GeohashGridBody(String field, Integer precision) {
    }
}
