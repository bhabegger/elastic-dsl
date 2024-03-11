package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticIpPrefixAggregation extends ElasticAggregations {
    @JsonProperty("ip_prefix")
    private final IpPrefixBody ipPrefix;

    ElasticIpPrefixAggregation(
        @JsonProperty("ip_prefix")
        IpPrefixBody ipPrefix
    ) {
        this.ipPrefix = ipPrefix;
    }

    public ElasticIpPrefixAggregation withIpV6() {
        return withBody((original) ->
            new IpPrefixBody(
                original.field,
                original.prefixLength,
                true,
                original.appendPrefixLength,
                original.minDocCount,
                original.keyed
            ));
    }

    public ElasticIpPrefixAggregation withAppendPrefixLength() {
        return withBody((original) ->
            new IpPrefixBody(
                original.field,
                original.prefixLength,
                original.isIpV6,
                true,
                original.minDocCount,
                original.keyed
            ));
    }
    public ElasticIpPrefixAggregation withMinDocCount(int minDocCount) {
        return withBody((original) ->
            new IpPrefixBody(
                original.field,
                original.prefixLength,
                original.isIpV6,
                original.appendPrefixLength,
                minDocCount,
                original.keyed
            ));
    }
    public ElasticIpPrefixAggregation withKeyed() {
        return withBody((original) ->
            new IpPrefixBody(
                original.field,
                original.prefixLength,
                original.isIpV6,
                original.appendPrefixLength,
                original.minDocCount,
                true
            ));
    }

    private ElasticIpPrefixAggregation withBody(Function<IpPrefixBody, IpPrefixBody> update) {
        return new ElasticIpPrefixAggregation(update.apply(this.ipPrefix));
    }

    public static ElasticIpPrefixAggregation ipPrefix(String field, Integer prefixLength) {
        return new ElasticIpPrefixAggregation(
            new IpPrefixBody(field, prefixLength,
                null, null, null, null)
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record IpPrefixBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("prefix_length")
        Integer prefixLength,
        @JsonProperty("is_ipv6")
        Boolean isIpV6,
        @JsonProperty("append_prefix_length")
        Boolean appendPrefixLength,
        @JsonProperty("min_doc_count")
        Integer minDocCount,
        @JsonProperty("keyed")
        Boolean keyed
    ) {
    }

}
