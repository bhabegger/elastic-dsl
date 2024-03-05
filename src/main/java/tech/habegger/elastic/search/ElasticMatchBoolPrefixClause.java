package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticMatchBoolPrefixClause implements ElasticSearchClause {

    @JsonProperty("match_bool_prefix")
    private final Map<String, String> matchBoolPrefix;

    ElasticMatchBoolPrefixClause(Map<String, String> matchBoolPrefix) {
        this.matchBoolPrefix = matchBoolPrefix;
    }
    public static ElasticMatchBoolPrefixClause matchBoolPrefix(String field, String queryString) {
        return new ElasticMatchBoolPrefixClause(Map.of(field, queryString));
    }
}
