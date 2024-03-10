package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ElasticNestedAggregation extends ElasticAggregations {

    @JsonProperty("nested")
    final NestedBody nested;

    ElasticNestedAggregation(NestedBody nested) {
        this.nested = nested;
    }

    public static ElasticNestedAggregation nestedAgg(String path) {
        return new ElasticNestedAggregation( new NestedBody(path));
    }

    record NestedBody(String path) {
    }
}
