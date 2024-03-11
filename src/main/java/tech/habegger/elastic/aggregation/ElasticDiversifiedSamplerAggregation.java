package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticDiversifiedSamplerAggregation extends ElasticAggregations {
    @JsonProperty("diversified_sampler")
    private final DiversifiedSamplerBody diversifiedSampler;

    ElasticDiversifiedSamplerAggregation(
            DiversifiedSamplerBody diversifiedSampler
    ) {
        this.diversifiedSampler = diversifiedSampler;
    }

    public ElasticDiversifiedSamplerAggregation withMaxDocsPerValue(Integer maxDocsPerValue) {
        return withBody(original -> new DiversifiedSamplerBody(
                original.field,
                original.shardSize,
                maxDocsPerValue,
                original.executionHint
        ));
    }
    public ElasticDiversifiedSamplerAggregation withExecutionHint(ExecutionHint executionHint) {
        return withBody(original -> new DiversifiedSamplerBody(
                original.field,
                original.shardSize,
                original.maxDocsPerValue,
                executionHint
        ));
    }

    private ElasticDiversifiedSamplerAggregation withBody(Function<DiversifiedSamplerBody, DiversifiedSamplerBody> update) {
        return new ElasticDiversifiedSamplerAggregation(update.apply(this.diversifiedSampler));
    }

    public static ElasticDiversifiedSamplerAggregation diversifiedSampler(String field, int shardSize) {
        return new ElasticDiversifiedSamplerAggregation(new DiversifiedSamplerBody(field, shardSize, null, null));
    }
    public static ElasticDiversifiedSamplerAggregation diversifiedSampler(String field, int shardSize, Map<String, ElasticAggregations> aggregations) {
        return new ElasticDiversifiedSamplerAggregation(new DiversifiedSamplerBody(field, shardSize, null, null));
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record DiversifiedSamplerBody(
        @JsonProperty("point")
        String field,
        @JsonProperty("shard_size")
        Integer shardSize,
        @JsonProperty("max_docs_per_value")
        Integer maxDocsPerValue,
        @JsonProperty("execution_hint")
        ExecutionHint executionHint
    ) { }

    public enum ExecutionHint {
        map,
        global_ordinals,
        bytes_hash
    }
}
