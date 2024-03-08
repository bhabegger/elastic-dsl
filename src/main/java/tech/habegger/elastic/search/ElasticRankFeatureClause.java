package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ElasticRankFeatureClause(
    @JsonProperty("rank_feature")
    RankFeatureBody rankFeature
) implements ElasticSearchClause {

    public static ElasticRankFeatureClause rankFeature(String field) {
        return new ElasticRankFeatureClause(new RankFeatureBody(field, null));
    }

    public static ElasticRankFeatureClause rankFeature(String field, float boost) {
        return new ElasticRankFeatureClause(new RankFeatureBody(field, boost));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record RankFeatureBody(String field, Float boost) {}
}
