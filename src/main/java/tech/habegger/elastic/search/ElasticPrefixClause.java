package tech.habegger.elastic.search;

import java.util.Map;

public class ElasticPrefixClause implements ElasticSearchClause {
    private final Map<String, String> prefix;

    ElasticPrefixClause(Map<String, String> prefix) {
        this.prefix = prefix;
    }
    public static ElasticPrefixClause prefix(String field, String queryString) {
        return new ElasticPrefixClause(Map.of(field, queryString));
    }
    public Map<String, String> getPrefix() {
        return prefix;
    }
}
