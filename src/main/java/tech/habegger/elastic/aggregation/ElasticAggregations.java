package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ElasticAggregations {
    Map<String, ElasticAggregations> aggregations = null;

    public ElasticAggregations aggregation(String name, ElasticAggregations aggregation) {
        if(this.aggregations == null) {
            this.aggregations = new HashMap<>();
        }
        this.aggregations.put(name, aggregation);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        return Objects.deepEquals(this, obj);
    }
}
