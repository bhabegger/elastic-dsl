package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticSignificantTermsAggregation extends ElasticAggregations {

    @JsonProperty("significant_terms")
    private final SignificantTermsBody significantTerms;

    ElasticSignificantTermsAggregation(
            SignificantTermsBody significantTerms,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.significantTerms = significantTerms;
    }
    private static ElasticSignificantTermsAggregation internalSignificantTerms(String field, Integer size, Map<String, ElasticAggregations> aggregations) {
        return new ElasticSignificantTermsAggregation(new SignificantTermsBody(field, size), aggregations);
    }
    public static ElasticSignificantTermsAggregation significantTerms(String field, int size, Map<String, ElasticAggregations> aggregations) {
        return internalSignificantTerms(field, size, aggregations);
    }
    public static ElasticAggregations significantTerms(String field, int size) {
        return internalSignificantTerms(field, size, null);
    }
    public static ElasticAggregations significantTerms(String field) {
        return internalSignificantTerms(field, null, null);
    }


@JsonInclude(JsonInclude.Include.NON_NULL)
private record SignificantTermsBody(String field, Integer size) {
    }
}
