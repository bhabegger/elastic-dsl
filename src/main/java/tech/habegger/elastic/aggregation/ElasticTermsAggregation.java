package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticTermsAggregation extends ElasticAggregations {
    @JsonProperty("terms")
    private final TermsBody terms;

    ElasticTermsAggregation(
            TermsBody terms,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.terms = terms;
    }
    public static ElasticAggregations termsAgg(String field, int size, Map<String, ElasticAggregations> aggregations) {
        return new ElasticTermsAggregation(new TermsBody(field, size), aggregations);
    }
    public static ElasticAggregations termsAgg(String field, int size) {
        return termsAgg(field, size, null);
    }
    public static ElasticAggregations termsAgg(String field) {
        return new ElasticTermsAggregation(new TermsBody(field, null), null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record TermsBody(String field, Integer size) {
    }
}
