package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticMaxAggregation extends ElasticAggregations {
    private final MaxBody max;

    ElasticMaxAggregation(
            MaxBody max,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.max = max;
    }

    public static ElasticAggregations maxAggregation(String field) {
        return new ElasticMaxAggregation(new MaxBody(field), null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElasticMaxAggregation) obj;
        return Objects.equals(this.max, that.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(max);
    }

    private record MaxBody(String field) {
    }
}
