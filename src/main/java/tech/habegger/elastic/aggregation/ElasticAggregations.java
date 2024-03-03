package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ElasticAggregations {
    private final Map<String, ElasticAggregations> aggregations;

    protected ElasticAggregations(Map<String, ElasticAggregations> aggregations) {
        this.aggregations = aggregations;
    }

    public static Builder newAggregations() {
        return new Builder();
    }
    public static class Builder {
        private final Map<String, ElasticAggregations> aggregations = new HashMap<>();

        public Builder add(String name, ElasticAggregations aggregation) {
            aggregations.put(name, aggregation);
            return this;
        }
        public Map<String, ElasticAggregations> build() {
            return aggregations;
        }
    }
}
