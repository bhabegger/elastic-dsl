package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.TimeUnit;

import java.util.Map;
import java.util.function.Function;

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

    public ElasticAutoDateHistogramAggregation withFormat(String format) {
        return withBody((original) ->
           new AutoDateHistogramBody(
               original.field,
               original.buckets,
               format,
               original.minimumInterval,
               original.missing
           ));
    }

    public ElasticAggregations withMinimumInterval(TimeUnit minimumInterval) {
        return withBody((original) ->
            new AutoDateHistogramBody(
                original.field,
                original.buckets,
                original.format,
                minimumInterval,
                original.missing
            ));
    }

    public ElasticAggregations withMissing(String defaultValue) {
        return withBody((original) ->
                new AutoDateHistogramBody(
                        original.field,
                        original.buckets,
                        original.format,
                        original.minimumInterval,
                        defaultValue
                ));
    }
    private ElasticAutoDateHistogramAggregation withBody(Function<AutoDateHistogramBody, AutoDateHistogramBody> update) {
        return new ElasticAutoDateHistogramAggregation(update.apply(this.autoDateHistogramAggregation), this.aggregations);
    }

    public static ElasticAutoDateHistogramAggregation autoDateHistogram(String field, int buckets) {
        return new ElasticAutoDateHistogramAggregation(
                new AutoDateHistogramBody(field, buckets, null, null, null),
                null
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record AutoDateHistogramBody(
            @JsonProperty("field")
            String field,
            @JsonProperty("buckets")
            int buckets,
            @JsonProperty("format")
            String format,
            @JsonProperty("minimum_interval")
            TimeUnit minimumInterval,
            @JsonProperty("missing")
            String missing
    ) { }

}
