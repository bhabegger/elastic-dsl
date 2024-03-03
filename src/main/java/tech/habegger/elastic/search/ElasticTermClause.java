package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticTermClause implements ElasticSearchClause {
    @JsonProperty("term")
    private final Map<String, String> term;

    ElasticTermClause(Map<String, String> term) {
        this.term = term;
    }
    public static ElasticTermClause term(String field, String queryString) {
        return new ElasticTermClause(Map.of(field, queryString));
    }
}
