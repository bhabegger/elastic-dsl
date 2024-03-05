package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class ElasticMatchPhrasePrefixClause implements ElasticSearchClause {

    @JsonProperty("match_phrase_prefix")
    private final Map<String, String> matchPhrasePrefix;

    ElasticMatchPhrasePrefixClause(Map<String, String> matchPhrasePrefix) {
        this.matchPhrasePrefix = matchPhrasePrefix;
    }
    public static ElasticMatchPhrasePrefixClause matchPhrasePrefix(String field, String queryString) {
        return new ElasticMatchPhrasePrefixClause(Map.of(field, queryString));
    }
}
