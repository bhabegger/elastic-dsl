package tech.habegger.elastic.analysis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech.habegger.elastic.analysis.filters.ElasticFilterDefinition;

import java.util.LinkedHashMap;
import java.util.Map;

import static tech.habegger.elastic.shared.Helpers.nullIfEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticAnalysisDefinition(
    String type,
    Map<String, ElasticAnalyzerDefinition> analyzer,
    Map<String, ElasticTokenizerDefinition> tokenizer,
    @JsonProperty("char_filter")
    Map<String, ElasticCharFilterDefinition> charFilter,
    Map<String, ElasticFilterDefinition> filter,
    @JsonProperty("position_increment_gap")
    Integer positionIncrementGap
) {

    public static Builder analysis() {
        return new Builder();
    }

    public static class Builder {
        String type;
        Map<String, ElasticAnalyzerDefinition> analyzer = new LinkedHashMap<>();
        Map<String, ElasticTokenizerDefinition> tokenizer = new LinkedHashMap<>();
        Map<String, ElasticCharFilterDefinition> charFilter = new LinkedHashMap<>();
        Map<String, ElasticFilterDefinition> filter = new LinkedHashMap<>();
        Integer positionIncrementGap;

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withAnalyzer(String name, ElasticAnalyzerDefinition definition) {
            analyzer.put(name, definition);
            return this;
        }

        public Builder withTokenizer(String name, ElasticTokenizerDefinition definition) {
            tokenizer.put(name, definition);
            return this;
        }

        public Builder withFilter(String name, ElasticFilterDefinition definition) {
            filter.put(name, definition);
            return this;
        }

        public Builder withCharFilter(String name, ElasticCharFilterDefinition definition) {
            charFilter.put(name, definition);
            return this;
        }

        public Builder withPositionIncrementGap(Integer positionIncrementGap) {
            this.positionIncrementGap = positionIncrementGap;
            return this;
        }

        public ElasticAnalysisDefinition build() {
            return new ElasticAnalysisDefinition(
                type,
                nullIfEmpty(analyzer),
                nullIfEmpty(tokenizer),
                nullIfEmpty(charFilter),
                nullIfEmpty(filter),
                positionIncrementGap
            );
        }

    }
}
