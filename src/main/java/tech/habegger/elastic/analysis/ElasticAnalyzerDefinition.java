package tech.habegger.elastic.analysis;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tech.habegger.elastic.shared.Helpers.nullIfEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ElasticAnalyzerDefinition(
    String tokenizer,
    List<String> filter
) {

    public static Builder analyzer() {
        return new Builder();
    }

    public static class Builder {
        String tokenizer;
        List<String> filter = new ArrayList<>();

        public ElasticAnalyzerDefinition build()  {
            return new ElasticAnalyzerDefinition(
                tokenizer,
                nullIfEmpty(filter)
            );
        }

        public Builder withTokenizer(String tokenizer) {
            this.tokenizer = tokenizer;
            return this;
        }

        public Builder withFilters(String... filters) {
            this.filter.addAll(Arrays.stream(filters).toList());
            return this;
        }
    }
}
