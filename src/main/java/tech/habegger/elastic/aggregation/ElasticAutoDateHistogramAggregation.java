package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticAutoDateHistogramAggregation extends ElasticAggregations {
    @JsonProperty("auto_date_histogram")
    private final AutoDateHistogramBody autoDateHistogramAggregation;

    ElasticAutoDateHistogramAggregation(
            @JsonProperty("auto_date_histogram")
            AutoDateHistogramBody autoDateHistogramAggregation,
            Map<String, ElasticAggregations> aggregations

    ) {
        super(aggregations);
        this.autoDateHistogramAggregation = autoDateHistogramAggregation;
    }

    public static ElasticAggregations autoDateHistogramAggregation(String field, int buckets) {
        return new ElasticAutoDateHistogramAggregation(new AutoDateHistogramBody(field, buckets),null);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElasticAutoDateHistogramAggregation) obj;
        return Objects.equals(this.autoDateHistogramAggregation, that.autoDateHistogramAggregation);
    }

    private record AutoDateHistogramBody(String field, int buckets) {
    }
}
