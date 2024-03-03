package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticMatchPhraseClause implements ElasticSearchClause {

    @JsonProperty("match_phrase")
    private final Map<String, String> matchPhrase;

    ElasticMatchPhraseClause(Map<String, String> match) {
        this.matchPhrase = match;
    }
    public static ElasticMatchPhraseClause matchPhrase(String field, String queryString) {
        return new ElasticMatchPhraseClause(Map.of(field, queryString));
    }
}
