package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticNestedClause implements ElasticSearchClause {
    @JsonProperty("nested")
    private final NestedBody nested;

    ElasticNestedClause(NestedBody nested) {
        this.nested = nested;
    }
    public static ElasticNestedClause nested(String path, ElasticSearchClause query) {
        return nested(path, query, null, null);
    }
    public static ElasticNestedClause nested(String path, ElasticSearchClause query, ScoreMode scoreMode) {
        return nested(path, query, scoreMode, null);
    }

    public static ElasticNestedClause nested(String path, ElasticSearchClause query, Boolean ignoreUnmapped) {
        return nested(path, query, null, ignoreUnmapped);
    }
    public static ElasticNestedClause nested(String path, ElasticSearchClause query, ScoreMode scoreMode, Boolean ignoreUnmapped) {
        return new ElasticNestedClause(new NestedBody(path, query, scoreMode, ignoreUnmapped));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record NestedBody(
            String path,
            ElasticSearchClause query,
            @JsonProperty("score_mode")
            ScoreMode scoreMode,
            @JsonProperty("ignore_unmapped")
            Boolean ignoreUnmapped
    ) {}

    public enum ScoreMode {
        avg,
        max,
        min,
        none,
        sum
    }
}
