package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticMatchClause implements ElasticSearchClause {

    @JsonProperty("match")
    private final Map<String, String> match;

    ElasticMatchClause(Map<String, String> match) {
        this.match = match;
    }
    public static ElasticMatchClause match(String field, String queryString) {
        return new ElasticMatchClause(Map.of(field, queryString));
    }
}
