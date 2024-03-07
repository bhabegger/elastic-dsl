package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticConstantScoreClause(
        @JsonProperty("constant_score")
        ConstantScoreBody constantScore
) implements ElasticSearchClause {

    public static ElasticConstantScoreClause constantScore(ElasticSearchClause filter) {
        return constantScore(filter, null);
    }
    public static ElasticConstantScoreClause constantScore(ElasticSearchClause filter, Float boost) {
        return new ElasticConstantScoreClause(new ConstantScoreBody(filter, boost));
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private record ConstantScoreBody(
        ElasticSearchClause filter,
        Float boost) {
    }
}
