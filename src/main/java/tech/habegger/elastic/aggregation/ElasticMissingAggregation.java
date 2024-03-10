package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ElasticMissingAggregation extends ElasticAggregations {

    @JsonProperty("missing")
    final MissingBody missing;

    ElasticMissingAggregation(MissingBody missing) {
        this.missing = missing;
    }

    public static ElasticMissingAggregation missing(String field) {
        return new ElasticMissingAggregation( new MissingBody(field));
    }

    record MissingBody(String field) {
    }
}
