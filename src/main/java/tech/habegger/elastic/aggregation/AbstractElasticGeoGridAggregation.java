package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.GeoRect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractElasticGeoGridAggregation extends ElasticAggregations {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record GeoGridAggregationBody(
        String field,
        Integer precision,
        GeoRect bounds,
        Integer size,
        @JsonProperty("shard_size")
        Integer shardSize
    ) {

        public GeoGridAggregationBody withBounds(GeoRect bounds) {
            return new GeoGridAggregationBody(
                this.field,
                this.precision,
                bounds,
                this.size,
                this.shardSize
            );
        }

        public GeoGridAggregationBody withSize(Integer size) {
            return new GeoGridAggregationBody(
                this.field,
                this.precision,
                this.bounds,
                size,
                this.shardSize
            );
        }

        public GeoGridAggregationBody withShardSize(Integer shardSize) {
            return new GeoGridAggregationBody(
                this.field,
                this.precision,
                this.bounds,
                this.size,
                shardSize
            );
        }

    }
}
