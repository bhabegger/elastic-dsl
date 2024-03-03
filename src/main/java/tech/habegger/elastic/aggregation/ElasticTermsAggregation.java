package tech.habegger.elastic.aggregation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ElasticTermsAggregation extends ElasticAggregations {
    private final TermsBody terms;

    ElasticTermsAggregation(
            TermsBody terms,
            Map<String, ElasticAggregations> aggregations
    ) {
        super(aggregations);
        this.terms = terms;
    }
    public static ElasticAggregations termsAggregation(String field, int size, Map<String, ElasticAggregations> aggregations) {
        return new ElasticTermsAggregation(new TermsBody(field, size), aggregations);
    }
    public static ElasticAggregations termsAggregation(String field, int size) {
        return termsAggregation(field, size, null);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ElasticTermsAggregation) obj;
        return Objects.equals(this.terms, that.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }

    private record TermsBody(String field, Integer size) {
    }
}
