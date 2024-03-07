package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticDisMaxClause(
        @JsonProperty("dis_max")
        DisMaxBody constantScore
) implements ElasticSearchClause {

    public static ElasticDisMaxClause disMax(Float tieBreaker, ElasticSearchClause... queries) {
        return new ElasticDisMaxClause(new DisMaxBody(Arrays.asList(queries), tieBreaker));
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record DisMaxBody(
        List<ElasticSearchClause> queries,
        @JsonProperty("tie_breaker")
        Float tieBreaker) {
    }
}
