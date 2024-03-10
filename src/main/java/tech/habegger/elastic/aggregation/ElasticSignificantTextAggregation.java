package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticSignificantTextAggregation extends ElasticAggregations {

    @JsonProperty("significant_text")
    private final SignificantTextBody significantText;

    ElasticSignificantTextAggregation(
            SignificantTextBody significantText
    ) {
        this.significantText = significantText;
    }
    public static ElasticSignificantTextAggregation significantText(String field) {
        return new ElasticSignificantTextAggregation(new SignificantTextBody(
            field, null, null, null, null, null, null
        ));
    }
    private ElasticSignificantTextAggregation withBody(Function<SignificantTextBody, SignificantTextBody> update) {
        return new ElasticSignificantTextAggregation(update.apply(this.significantText));
    }

    public ElasticSignificantTextAggregation withFilterDuplicateText() {
        return withBody(original -> new SignificantTextBody(
            original.field,
            true,
            original.minDocCount,
            original.size,
            original.shardSize,
            original.include,
            original.exclude
        ));
    }

    public ElasticSignificantTextAggregation withMinDocCount(Integer minDocCount) {
        return withBody(original -> new SignificantTextBody(
            original.field,
            original.filterDuplicateText,
            minDocCount,
            original.size,
            original.shardSize,
            original.include,
            original.exclude
        ));
    }

    public ElasticSignificantTextAggregation withSize(Integer size) {
        return withBody(original -> new SignificantTextBody(
            original.field,
            original.filterDuplicateText,
            original.minDocCount,
            size,
            original.shardSize,
            original.include,
            original.exclude
        ));
    }

    public ElasticSignificantTextAggregation withInclude(String... include) {
        return withBody(original -> new SignificantTextBody(
            original.field,
            original.filterDuplicateText,
            original.minDocCount,
            original.size,
            original.shardSize,
            Arrays.asList(include),
            original.exclude
        ));
    }

    public ElasticSignificantTextAggregation withExclude(String... exclude) {
        return withBody(original -> new SignificantTextBody(
            original.field,
            original.filterDuplicateText,
            original.minDocCount,
            original.size,
            original.shardSize,
            original.include,
            Arrays.asList(exclude)
        ));
    }

    public ElasticSignificantTextAggregation withShardSize(String shardSize) {
        return withBody(original -> new SignificantTextBody(
            original.field,
            original.filterDuplicateText,
            original.minDocCount,
            original.size,
            shardSize,
            original.include,
            original.exclude
        ));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record SignificantTextBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("filter_duplicate_text")
        Boolean filterDuplicateText,
        @JsonProperty("max_doc_count")
        Integer minDocCount,
        @JsonProperty("size")
        Integer size,
        @JsonProperty("shard_size")
        String shardSize,
        @JsonProperty("include")
        List<String> include,
        @JsonProperty("exclude")
        List<String> exclude
    ) { }
}
