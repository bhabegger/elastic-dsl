package tech.habegger.elastic.analysis.filters;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ElasticDictionaryDecompounderTokenFilter(
    String wordListPath,
    Integer maxSubwordSize,
    Integer minSubwordSize,
    Integer minWordSize,
    Boolean onlyLongestMatch
) implements ElasticFilterDefinition {

    @JsonProperty("type")
    public String type() {
        return "dictionary_decompounder";
    }

    public static Builder dictionaryDecompounderFilter() {
        return new Builder();
    }

    public static class Builder {
        String wordListPath;
        Integer maxSubwordSize;
        Integer minSubwordSize;
        Integer minWordSize;
        Boolean onlyLongestMatch;

        public Builder withWordListPath(String wordListPath) {
            this.wordListPath = wordListPath;
            return this;
        }

        public Builder withMaxSubwordSize(Integer maxSubwordSize) {
            this.maxSubwordSize = maxSubwordSize;
            return this;
        }

        public Builder withMinSubwordSize(Integer minSubwordSize) {
            this.minSubwordSize = minSubwordSize;
            return this;
        }

        public Builder withMinWordSize(Integer minWordSize) {
            this.minWordSize = minWordSize;
            return this;
        }

        public Builder withOnlyLongestMatch(Boolean onlyLongestMatch) {
            this.onlyLongestMatch = onlyLongestMatch;
            return this;
        }

        public ElasticFilterDefinition build() {
            return new ElasticDictionaryDecompounderTokenFilter(
                wordListPath,
                maxSubwordSize,
                minSubwordSize,
                minWordSize,
                onlyLongestMatch
            );
        }
    }
}
