package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticSignificantTermsAggregation extends ElasticAggregations {

    @JsonProperty("significant_terms")
    private final SignificantTermsBody significantTerms;

    ElasticSignificantTermsAggregation(
            SignificantTermsBody significantTerms
    ) {
        this.significantTerms = significantTerms;
    }
    private static ElasticSignificantTermsAggregation internalSignificantTerms(String field, Integer size) {
        return new ElasticSignificantTermsAggregation(new SignificantTermsBody(field, size, null));
    }
    public static ElasticSignificantTermsAggregation significantTerms(String field, int size) {
        return internalSignificantTerms(field, size);
    }
    public static ElasticSignificantTermsAggregation significantTerms(String field) {
        return internalSignificantTerms(field, null);
    }
    private ElasticSignificantTermsAggregation withBody(Function<SignificantTermsBody, SignificantTermsBody> update) {
        return new ElasticSignificantTermsAggregation(update.apply(this.significantTerms));
    }

    public ElasticAggregations withExclude(String... exclude) {
        return withBody(original -> new SignificantTermsBody(
            original.field,
            original.size,
            Arrays.asList(exclude)
        ));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record SignificantTermsBody(String field, Integer size, List<String> exclude) {
    }
}
