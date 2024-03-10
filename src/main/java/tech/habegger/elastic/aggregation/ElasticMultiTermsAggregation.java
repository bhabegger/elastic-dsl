package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.shared.CollectMode;
import tech.habegger.elastic.shared.OrderDirection;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticMultiTermsAggregation extends ElasticAggregations {
    @JsonProperty("multi_terms")
    private final MultiTermsBody multiTerms;

    ElasticMultiTermsAggregation(
        MultiTermsBody multiTerms
    ) {
        this.multiTerms = multiTerms;
    }

    private ElasticMultiTermsAggregation withBody(Function<MultiTermsBody, MultiTermsBody> update) {
        return new ElasticMultiTermsAggregation(update.apply(this.multiTerms));
    }

    public ElasticMultiTermsAggregation withSize(Integer size) {
        return withBody(original -> new MultiTermsBody(
            original.terms,
            size,
            original.shardSize,
            original.showTermDocCountError,
            original.order,
            original.shardMinDocCount,
            original.minDocCount,
            original.collectMode
        ));
    }
    public ElasticMultiTermsAggregation withShardSize(Integer shardSize) {
        return withBody(original -> new MultiTermsBody(
            original.terms,
            original.size,
            shardSize,
            original.showTermDocCountError,
            original.order,
            original.shardMinDocCount,
            original.minDocCount,
            original.collectMode
        ));
    }
    public ElasticMultiTermsAggregation withShowTermDocCountError() {
        return withBody(original -> new MultiTermsBody(
            original.terms,
            original.size,
            original.shardSize,
            true,
            original.order,
            original.minDocCount,
            original.shardMinDocCount,
            original.collectMode
        ));
    }
    public ElasticMultiTermsAggregation withOrder(String field, OrderDirection direction) {
        return withBody(original -> new MultiTermsBody(
            original.terms,
            original.size,
            original.shardSize,
            original.showTermDocCountError,
            Map.of(field, direction),
            original.minDocCount,
            original.shardMinDocCount,
            original.collectMode
        ));
    }

    public ElasticMultiTermsAggregation withMinDocCount(Integer minDocCount) {
        return withBody(original -> new MultiTermsBody(
            original.terms,
            original.size,
            original.shardSize,
            original.showTermDocCountError,
            original.order,
            minDocCount,
            original.shardMinDocCount,
            original.collectMode
        ));
    }

    public ElasticMultiTermsAggregation withShardMinDocCount(Integer shardMinDocCount) {
        return withBody(original -> new MultiTermsBody(
            original.terms,
            original.size,
            original.shardSize,
            original.showTermDocCountError,
            original.order,
            original.minDocCount,
            shardMinDocCount,
            original.collectMode
        ));
    }

    public ElasticMultiTermsAggregation withCollectMode(CollectMode collectMode) {
        return withBody(original -> new MultiTermsBody(
            original.terms,
            original.size,
            original.shardSize,
            original.showTermDocCountError,
            original.order,
            original.minDocCount,
            original.shardMinDocCount,
            collectMode
        ));
    }

    public static ElasticMultiTermsAggregation multiTerms(TermSpec... fields) {
        return new ElasticMultiTermsAggregation(
            new MultiTermsBody(
                Arrays.asList(fields),
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        );
    }

    public static ElasticMultiTermsAggregation multiTerms(String... fields) {
        return new ElasticMultiTermsAggregation(
            new MultiTermsBody(
                Arrays.stream(fields).map(f -> new TermSpec(f, null)).toList(),
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record MultiTermsBody(
        @JsonProperty("terms")
        List<TermSpec> terms,

        @JsonProperty("size")
        Integer size,

        @JsonProperty("shard_size")
        Integer shardSize,

        @JsonProperty("show_term_doc_count_error")
        Boolean showTermDocCountError,

        @JsonProperty("order")
        Map<String, OrderDirection> order,

        @JsonProperty("min_doc_count")
        Integer minDocCount,

        @JsonProperty("shard_min_doc_count")
        Integer shardMinDocCount,

        @JsonProperty("collect_mode")
        CollectMode collectMode

    ) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TermSpec(String field, String missing) {
        TermSpec withMissing(String value) {
            return new TermSpec(this.field, value);
        }
    }

    public static TermSpec termSpec(String name) {
        return new TermSpec(name, null);
    }

    public static TermSpec termSpec(String name, String missing) {
        return new TermSpec(name, missing);
    }

}
