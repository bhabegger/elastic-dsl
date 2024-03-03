package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticTermsClause implements ElasticSearchClause {
    @JsonProperty("terms")
    private final Map<String, List<String>> terms;

    ElasticTermsClause(Map<String, List<String>> terms) {
        this.terms = terms;
    }
    public static ElasticTermsClause terms(String field, String... terms) {
        return new ElasticTermsClause(Map.of(field, Arrays.asList(terms)));
    }
}
