package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

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

    public static ElasticAggregations geohashGridAggregation(String field, Integer precision) {
        return geohashGridAggregation(field, precision, null);
    }
    public static ElasticAggregations geohashGridAggregation(String field, Integer precision, Map<String, ElasticAggregations> aggregations) {
        return new ElasticGeohashGridAggregation(new GeohashGridBody(field, precision), aggregations);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElasticGeohashGridAggregation) obj;
        return Objects.equals(this.geohash_grid, that.geohash_grid);
    }

    record GeohashGridBody(String field, Integer precision) {
    }
}
