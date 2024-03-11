package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public record ElasticBoostingClause(BoostingBody boosting) implements ElasticSearchClause {

    public static ElasticBoostingClause boosting(ElasticSearchClause positive, ElasticSearchClause negative, float negativeBoost) {
        return new ElasticBoostingClause(new BoostingBody(positive, negative, negativeBoost));
    }
    private record BoostingBody(
        ElasticSearchClause positive,
        ElasticSearchClause negative,
        @JsonProperty("negative_boost")
        double negativeBoost) {
    }
}
