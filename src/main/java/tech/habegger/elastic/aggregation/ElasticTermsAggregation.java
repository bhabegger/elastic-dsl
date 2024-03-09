package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticTermsAggregation extends ElasticAggregations {
    @JsonProperty("terms")
    private final TermsBody terms;

    ElasticTermsAggregation(
            TermsBody terms
    ) {
        this.terms = terms;
    }
    public static ElasticAggregations termsAgg(String field, int size) {
        return new ElasticTermsAggregation(new TermsBody(field, size));
    }
    public static ElasticAggregations termsAgg(String field) {
        return new ElasticTermsAggregation(new TermsBody(field, null));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record TermsBody(String field, Integer size) {
    }
}
