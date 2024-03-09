package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unused")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticCategorizeTextAggregation extends ElasticAggregations {
    @JsonProperty("categorize_text")
    private final CategorizeTextBody categorizeText;

    ElasticCategorizeTextAggregation(
            CategorizeTextBody categorizeText,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.categorizeText = categorizeText;
    }

    public ElasticCategorizeTextAggregation withAggregations(Map<String, ElasticAggregations> aggregations) {
        return new ElasticCategorizeTextAggregation(categorizeText, aggregations);
    }

    private ElasticCategorizeTextAggregation withBody(Function<CategorizeTextBody, CategorizeTextBody> update) {
        return new ElasticCategorizeTextAggregation(update.apply(this.categorizeText), this.aggregations);
    }

    public ElasticCategorizeTextAggregation withCategorizationFilters(String... categorizationFilters) {
        return withBody(original -> new CategorizeTextBody(
                original.field,
                Arrays.asList(categorizationFilters),
                original.categorizationAnalyzer,
                original.maxMatchedTokens,
                original.maxUniqueTokens,
                original.minDocCount,
                original.shardMinDocCount,
                original.similarityThreshold
        ));
    }
    public ElasticCategorizeTextAggregation withCategorizationAnalyzer(CategorizationAnalyzer categorizationAnalyzer) {
        return withBody(original -> new CategorizeTextBody(
                original.field,
                original.categorizationFilters,
                categorizationAnalyzer,
                original.maxMatchedTokens,
                original.maxUniqueTokens,
                original.minDocCount,
                original.shardMinDocCount,
                original.similarityThreshold
        ));
    }
    public ElasticCategorizeTextAggregation witMaxUniqueTokens(Integer maxUniqueTokens) {
        return withBody(original -> new CategorizeTextBody(
                original.field,
                original.categorizationFilters,
                original.categorizationAnalyzer,
                original.maxMatchedTokens,
                maxUniqueTokens,
                original.minDocCount,
                original.shardMinDocCount,
                original.similarityThreshold
        ));
    }

    public ElasticCategorizeTextAggregation withMinDocCount(Integer minDocCount) {
        return withBody(original -> new CategorizeTextBody(
                original.field,
                original.categorizationFilters,
                original.categorizationAnalyzer,
                original.maxMatchedTokens,
                original.maxUniqueTokens,
                minDocCount,
                original.shardMinDocCount,
                original.similarityThreshold
        ));
    }
    public ElasticCategorizeTextAggregation withShardMinDocCount(Integer shardMinDocCount) {
        return withBody(original -> new CategorizeTextBody(
                original.field,
                original.categorizationFilters,
                original.categorizationAnalyzer,
                original.maxMatchedTokens,
                original.maxUniqueTokens,
                original.minDocCount,
                shardMinDocCount,
                original.similarityThreshold
        ));
    }
    public ElasticCategorizeTextAggregation withMaxMatchedTokens(Integer maxMatchedTokens) {
        return withBody(original -> new CategorizeTextBody(
                original.field,
                original.categorizationFilters,
                original.categorizationAnalyzer,
                original.maxMatchedTokens,
                original.maxUniqueTokens,
                original.minDocCount,
                original.shardMinDocCount,
                original.similarityThreshold
        ));
    }
    public ElasticCategorizeTextAggregation withSimilarityThreshold(Integer similarityThreshold) {
        return withBody(original -> new CategorizeTextBody(
                original.field,
                original.categorizationFilters,
                original.categorizationAnalyzer,
                original.maxMatchedTokens,
                original.maxUniqueTokens,
                original.minDocCount,
                original.shardMinDocCount,
                similarityThreshold
        ));
    }

    public static ElasticCategorizeTextAggregation categorizeText(String field) {
        return new ElasticCategorizeTextAggregation(
            new CategorizeTextBody(
                field,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ),
            null
        );
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record CategorizeTextBody(
            String field,

            @JsonProperty("categorization_filters")
            List<String> categorizationFilters,

            @JsonProperty("categorization_analyzer")
            CategorizationAnalyzer categorizationAnalyzer,

            @JsonProperty("max_matched_tokens")
            Integer maxMatchedTokens,

            @JsonProperty("max_unique_tokens")
            Integer maxUniqueTokens,

            @JsonProperty("min_doc_count")
            Integer minDocCount,

            @JsonProperty("shard_min_doc_count")
            Integer shardMinDocCount,

            @JsonProperty("similarity_threshold")
            Integer similarityThreshold


    ) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record CategorizationAnalyzer(
        @JsonProperty("char_filter")
        List<String> charFilter,
        @JsonProperty("tokenizer")
        String tokenizer,
        @JsonProperty("filter")
        List<String> filter
    ) {
    }

}
