package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticRareTermsAggregation extends ElasticAggregations {

    @JsonProperty("rare_terms")
    private final RareTermsBody rareTerms;

    ElasticRareTermsAggregation(
            RareTermsBody rareTerms
    ) {
        this.rareTerms = rareTerms;
    }
    public static ElasticRareTermsAggregation rareTerms(String field) {
        return new ElasticRareTermsAggregation(new RareTermsBody(
            field, null, null, null, null, null
        ));
    }
    private ElasticRareTermsAggregation withBody(Function<RareTermsBody, RareTermsBody> update) {
        return new ElasticRareTermsAggregation(update.apply(this.rareTerms));
    }

    public ElasticRareTermsAggregation withMaxDocCount(Integer maxDocCount) {
        return withBody(original -> new RareTermsBody(
            original.field,
            maxDocCount,
            original.precision,
            original.include,
            original.exclude,
            original.missing
        ));
    }

    public ElasticRareTermsAggregation withPrecision(Float precision) {
        return withBody(original -> new RareTermsBody(
            original.field,
            original.maxDocCount,
            precision,
            original.include,
            original.exclude,
            original.missing
        ));
    }

    public ElasticRareTermsAggregation withInclude(String... include) {
        return withBody(original -> new RareTermsBody(
            original.field,
            original.maxDocCount,
            original.precision,
            Arrays.asList(include),
            original.exclude,
            original.missing
        ));
    }

    public ElasticRareTermsAggregation withExclude(String... exclude) {
        return withBody(original -> new RareTermsBody(
            original.field,
            original.maxDocCount,
            original.precision,
            original.include,
            Arrays.asList(exclude),
            original.missing
        ));
    }

    public ElasticRareTermsAggregation withMissing(String missing) {
        return withBody(original -> new RareTermsBody(
            original.field,
            original.maxDocCount,
            original.precision,
            original.include,
            original.exclude,
            missing
        ));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record RareTermsBody(
        @JsonProperty("field")
        String field,
        @JsonProperty("max_doc_count")
        Integer maxDocCount,
        @JsonProperty("precision")
        Float precision,
        @JsonProperty("include")
        List<String> include,
        @JsonProperty("exclude")
        List<String> exclude,
        @JsonProperty("missing")
        String missing
    ) { }
}
