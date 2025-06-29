package tech.habegger.elastic.analysis.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticShingleTokenFilter(
    @JsonProperty("min_shingle_size")
    Integer minShingleSize,
    @JsonProperty("max_shingle_size")
    Integer maxShingleSize,
    @JsonProperty("output_unigrams")
    Boolean outputUnigrams
) implements ElasticFilterDefinition {
    public String type() {
        return "shingle";
    }

    public static Builder shingleFilter() {
        return new Builder();
    }

    public static class Builder {
        Integer maxShingleSize;
        Integer minShingleSize;
        Boolean outputUnigrams;

        public Builder withMaxShingleSize(Integer maxShingleSize) {
            this.maxShingleSize = maxShingleSize;
            return this;
        }

        public Builder withMinShingleSize(Integer minShingleSize) {
            this.minShingleSize = minShingleSize;
            return this;
        }

        public Builder withOutputUnigrams(Boolean outputUnigrams) {
            this.outputUnigrams = outputUnigrams;
            return this;
        }

        public ElasticFilterDefinition build() {
            return new ElasticShingleTokenFilter(
                minShingleSize,
                maxShingleSize,
                outputUnigrams
            );
        }
    }
}
