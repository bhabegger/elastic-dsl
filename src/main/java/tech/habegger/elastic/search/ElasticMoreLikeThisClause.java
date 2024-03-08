package tech.habegger.elastic.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tech.habegger.elastic.search.DocumentReference.documentReference;

public record ElasticMoreLikeThisClause(
    @JsonProperty("more_like_this")
    MoreLikeThisBody moreLikeThis
) implements ElasticSearchClause {

    public static Builder newMoreLikeThis() {
        return new Builder();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record MoreLikeThisBody(
            @JsonProperty("fields")
            List<String> fields,
            @JsonProperty("like")
            List<Object> like,
            @JsonProperty("unlike")
            List<Object> unlike,
            @JsonProperty("max_query_terms")
            Integer maxQueryTerms,
            @JsonProperty("min_term_freq")
            Integer minTermFreq,
            @JsonProperty("min_doc_freq")
            Integer minDocFreq,
            @JsonProperty("max_doc_freq")
            Integer maxDocFreq,
            @JsonProperty("min_word_length")
            Integer minWordLength,
            @JsonProperty("max_word_length")
            Integer maxWordLength,
            @JsonProperty("stop_words")
            List<String> stopWords,
            @JsonProperty("analyzer")
            String analyzer,
            @JsonProperty("minimum_should_match")
            String minimumShouldMatch,
            @JsonProperty("fail_on_unsupported_field")
            Boolean failOnUnsupportedField,
            @JsonProperty("boost_terms")
            Float boostTerms,
            @JsonProperty("include")
            Boolean include,
            @JsonProperty("boost")
            Float boost
) {
}

    @SuppressWarnings({"UnusedReturnValue", "FieldCanBeLocal","unused"})
    public static class Builder {
        private final List<String> fields = new ArrayList<>();
        private final List<Object> like = new ArrayList<>();
        private final List<Object> unlike = new ArrayList<>();
        private Integer maxQueryTerms;
        private Integer minTermFreq;
        private Integer minDocFreq;
        private Integer maxDocFreq;
        private Integer minWordLength;
        private Integer maxWordLength;
        private final List<String> stopWords = new ArrayList<>();
        private String analyzer;
        private String minimumShouldMatch;
        private Boolean failOnUnsupportedField;
        private Float boostTerms;
        private Boolean include;
        private Float boost;

        public Builder fields(String... fields) {
            this.fields.addAll(Arrays.asList(fields));
            return this;
        }
        public Builder like(String text) {
            like.add(text);
            return this;
        }
        public Builder like(String indexId, String docId) {
            like.add(documentReference(indexId, docId));
            return this;
        }
        public Builder like(String indexId, Object inlineDoc) {
            like.add(new ArtificialDocument(indexId,inlineDoc));
            return this;
        }
        public Builder unlike(String text) {
            unlike.add(text);
            return this;
        }
        public Builder withMaxQueryTerms(int maxQueryTerms) {
            this.maxQueryTerms = maxQueryTerms;
            return this;
        }
        public Builder withMinTermFrequency(int minTermFreq) {
            this.minTermFreq = minTermFreq;
            return this;
        }
        public Builder withMinDocumentFrequency(int minDocFreq) {
            this.minDocFreq = minDocFreq;
            return this;
        }
        public Builder withMaxDocumentFrequency(int maxDocFreq) {
            this.maxDocFreq = maxDocFreq;
            return this;
        }
        public Builder withMinWordLength(int minWordLength) {
            this.minWordLength = minWordLength;
            return this;
        }
        public Builder withMaxWordLength(int maxWordLength) {
            this.maxWordLength = maxWordLength;
            return this;
        }
        public Builder stopWords(String... stopWords) {
            this.stopWords.addAll(Arrays.asList(stopWords));
            return this;
        }

        public Builder withAnalyzer(String analyzer) {
            this.analyzer = analyzer;
            return this;
        }

        public Builder minimumShouldMatch(String minimum) {
            minimumShouldMatch = minimum;
            return this;
        }
        public Builder  withoutFailureOnUnsupportedFields() {
            failOnUnsupportedField = false;
            return this;
        }
        public Builder  withTermBoostFactor(Float boostTerms) {
            this.boostTerms = boostTerms;
            return this;
        }
        public Builder withInclusionOfReferredDocuments() {
            this.include = true;
            return this;
        }
        public Builder withBoost(float boost) {
            this.boost = boost;
            return this;
        }

        public ElasticSearchClause build() {
            return new ElasticMoreLikeThisClause(new MoreLikeThisBody(
                nullIfEmpty(fields),
                like,
                nullIfEmpty(unlike),
                maxQueryTerms,
                minTermFreq,
                minDocFreq,
                maxDocFreq,
                minWordLength,
                maxWordLength,
                nullIfEmpty(stopWords),
                analyzer,
                minimumShouldMatch,
                failOnUnsupportedField,
                boostTerms,
                include,
                boost
            ));
        }

        private static <T> List<T> nullIfEmpty(List<T> clause) {
            if(clause.isEmpty()) {
                return null;
            } else {
                return clause;
            }
        }
    }
}
