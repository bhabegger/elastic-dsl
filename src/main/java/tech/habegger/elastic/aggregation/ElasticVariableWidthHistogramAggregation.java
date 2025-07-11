package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticVariableWidthHistogramAggregation extends ElasticAggregations {
    @JsonProperty("variable_width_histogram")
    private final VariableHistogramBody variableWidthHistogram;

    ElasticVariableWidthHistogramAggregation(
        @JsonProperty("variable_width_histogram")
        VariableHistogramBody variableWidthHistogram
    ) {
        this.variableWidthHistogram = variableWidthHistogram;
    }

    public ElasticVariableWidthHistogramAggregation withBuckets(int buckets) {
        return withBody((original) ->
            new VariableHistogramBody(
                original.field,
                buckets,
                original.initialBuffer,
                original.shardSize
            ));
    }

    public ElasticVariableWidthHistogramAggregation withInitialBuffer(int buffer) {
        return withBody((original) ->
            new VariableHistogramBody(
                original.field,
                original.buckets,
                buffer,
                original.shardSize
            ));
    }

    public ElasticVariableWidthHistogramAggregation withShardSize(int shardSize) {
        return withBody((original) ->
            new VariableHistogramBody(
                original.field,
                original.buckets,
                original.initialBuffer,
                shardSize
            ));
    }

    private ElasticVariableWidthHistogramAggregation withBody(Function<VariableHistogramBody, VariableHistogramBody> update) {
        return new ElasticVariableWidthHistogramAggregation(update.apply(this.variableWidthHistogram));
    }

    public static ElasticVariableWidthHistogramAggregation variableWidthHistogram(String field) {
        return new ElasticVariableWidthHistogramAggregation(
            new VariableHistogramBody(field,  null, null, null)
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record VariableHistogramBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("buckets")
        Integer buckets,
        @JsonProperty("initial_buffer")
        Integer initialBuffer,
        @JsonProperty("shard_size")
        Integer shardSize
    ) {
    }

}
