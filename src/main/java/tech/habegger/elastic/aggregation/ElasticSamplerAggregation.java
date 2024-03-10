package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class ElasticSamplerAggregation extends ElasticAggregations {

    @JsonProperty("sampler")
    final SamplerBody sampler;

    ElasticSamplerAggregation(SamplerBody sampler) {
        this.sampler = sampler;
    }

    public static ElasticSamplerAggregation sampler() {
        return new ElasticSamplerAggregation( new SamplerBody(null));
    }

    public static ElasticSamplerAggregation sampler(Integer shardSize) {
        return new ElasticSamplerAggregation( new SamplerBody(shardSize));
    }

    record SamplerBody(
        @JsonProperty("shard_size")
        Integer shardSize
    ) {
    }
}
