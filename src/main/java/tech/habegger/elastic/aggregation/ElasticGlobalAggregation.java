package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ElasticGlobalAggregation extends ElasticAggregations {


    @JsonProperty("global")
    final Map<String, String> global = Map.of();
    public static ElasticAggregations global() {
        return new ElasticGlobalAggregation();
    }
}
