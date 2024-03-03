package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticSumAggregation extends ElasticAggregations {
    private final SumBody sum;

    ElasticSumAggregation(
            SumBody sum,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.sum = sum;
    }

    public static ElasticAggregations sumAggregation(String field) {
        return new ElasticSumAggregation(new SumBody(field), null);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElasticSumAggregation) obj;
        return Objects.equals(this.sum, that.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sum);
    }

    private record SumBody(String field) {
    }
}
