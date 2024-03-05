package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticMultiMatchClause implements ElasticSearchClause {

    @JsonProperty("multi_match")
    private final MultiMatchBody multiMatch;

    ElasticMultiMatchClause(MultiMatchBody multiMatch) {
        this.multiMatch = multiMatch;
    }

    public static ElasticMultiMatchClause multiMatch(String queryString, String... fields) {
        return multiMatch(queryString, null, fields);
    }

    public static ElasticMultiMatchClause multiMatch(String queryString, MultiMatchType type, String... fields) {
        return new ElasticMultiMatchClause(new MultiMatchBody(queryString, Arrays.asList(fields), type));
    }

    private record MultiMatchBody(String query, List<String> fields, MultiMatchType type) {
    }

    public enum MultiMatchType {
        best_fields,
        most_fields,
        cross_fields,
        phrase,
        phrase_prefix,
        bool_prefix
    }
}
